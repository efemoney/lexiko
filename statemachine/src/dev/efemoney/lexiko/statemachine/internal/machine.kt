package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.ActionException
import dev.efemoney.lexiko.statemachine.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

internal class StateMachineImpl<StateT : Any, ActionT : Any>(
  initial: StateT,
  coroutineScope: CoroutineScope,
  private val definitions: Map<KClass<out StateT>, StateDefinition<StateT, StateT, ActionT>>,
) : StateMachine<StateT, ActionT>, CoroutineScope by coroutineScope {

  private val actions = MutableSharedFlow<ActionT>()
  override val state = MutableStateFlow(initial)

  init {
    actions
      .onEach(::handleAction)
      .launchIn(this)
  }

  override suspend fun process(action: ActionT) = runCatching { actions.emit(action) }.isSuccess

  private suspend fun handleAction(action: ActionT) = coroutineScope {
    val currentState = state.value
    val currentDefinition = definitionOf(currentState)

    val transition = currentDefinition.transitions.getOrElse(action::class) {
      actionError("No transition defined for action ($action) within state ($currentState)")
    }

    val newState = when (val transitionReturn = StateTransitionScope(currentState, action, this).transition()) {
      is ReturnNothing -> return@coroutineScope
      is ReturnT -> transitionReturn.state
    }
    val newDefinition = definitionOf(newState)

    // Run transition
    currentDefinition.exit(StateActionScope(currentState, this))
    state.value = newState
    newDefinition.enter(StateActionScope(newState, this))
  }

  private fun definitionOf(state: StateT) = definitions.getOrElse(state::class) {
    actionError("Could not find definition for current state ($state)")
  }
}

private fun actionError(message: String, cause: Throwable? = null): Nothing =
  throw ActionException(message, cause)
