package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.ActionException
import dev.efemoney.lexiko.statemachine.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

internal class StateMachineImpl<StateT : Any, ActionT : Any>(
  initial: StateT,
  coroutineScope: CoroutineScope,
  private val definitions: Map<KClass<out StateT>, StateDefinition<StateT, StateT, ActionT>>,
) : StateMachine<StateT, ActionT>, CoroutineScope by coroutineScope {

  private val actions = MutableSharedFlow<ActionT>()
  override val state = MutableStateFlow(initial)

  init {
    launch {
      val currentState = state.value
      val currentDefinition = definitionOf(currentState)
      currentDefinition.enter(StateActionScope(currentState, actions, this))

      actions
        .onEach(::handleAction)
        .collect()
    }
  }

  override suspend fun process(action: ActionT) = runCatching { actions.emit(action) }.isSuccess

  private suspend fun handleAction(action: ActionT) = coroutineScope {
    val currentState = state.value
    val currentDefinition = definitionOf(currentState)

    val transition = currentDefinition.transitions.getOrElse(action::class) {
      actionError("No transition defined for action ($action) within state ($currentState)")
    }

    // We have a transition, lets exit current state
    currentDefinition.exit(StateActionScope(currentState, actions, this))

    // Then run transition
    val newState = when (val result = transition(StateTransitionScope(currentState, action, this))) {
      ReturnNothing -> return@coroutineScope
      is ReturnT -> result.state
    }
    val newDefinition = definitionOf(newState)

    state.value = newState
    newDefinition.enter(StateActionScope(newState, actions, this))
  }

  private fun definitionOf(state: StateT) = definitions.getOrElse(state::class) {
    actionError("Could not find definition for current state ($state)")
  }
}

private fun actionError(message: String, cause: Throwable? = null): Nothing =
  throw ActionException(message, cause)
