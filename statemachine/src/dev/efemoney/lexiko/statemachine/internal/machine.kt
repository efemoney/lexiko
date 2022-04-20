package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.ActionException
import dev.efemoney.lexiko.statemachine.StateMachine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

internal class StateMachineImpl<StateT : Any, ActionT : Any>(
  initialState: StateT,
  private val coroutineScope: CoroutineScope,
  private val definitions: Map<KClass<out StateT>, StateDefinition<StateT, StateT, ActionT>>,
) : StateMachine<StateT, ActionT> {

  override val state = MutableStateFlow(initialState)
  override val actions = MutableSharedFlow<ActionT>()

  private val actionsJob = actions
    .onEach(::handleAction)
    .launchIn(coroutineScope)

  private val initJob = coroutineScope.launch {
    val currentState = state.value
    val currentDefinition = definitionFor(currentState)
    currentDefinition.enter(ActionScopeImpl(currentState, actions, coroutineScope))
  }

  override suspend fun cancel() {
    initJob.cancelAndJoin()
    actionsJob.cancelAndJoin()
  }

  private suspend fun handleAction(action: ActionT) = coroutineScope {
    val currentState = state.value
    val currentDefinition = definitionFor(currentState)

    val transitionScope = TransitionScopeImpl(currentState, action, coroutineScope)
    val transition = currentDefinition.findTransition(transitionScope)

    coroutineContext.ensureActive() // We have a transition, lets exit current state
    currentDefinition.exit(ActionScopeImpl(currentState, actions, coroutineScope))

    coroutineContext.ensureActive() // Then run transition
    val newState = when (val result = transitionScope.transition()) {
      ReturnNothing -> return@coroutineScope
      is ReturnT -> result.state
    }
    val newDefinition = definitionFor(newState)

    state.value = newState

    coroutineContext.ensureActive()
    newDefinition.enter(ActionScopeImpl(newState, actions, coroutineScope))
  }

  private fun definitionFor(state: StateT) = definitions.getOrElse(state::class) {
    actionError("Could not find definition for current state ($state)")
  }
}

internal class StateDefinition<SpecificStateT : StateT, StateT : Any, ActionT : Any>(
  private val enterAnyActions: List<StateAction<StateT, StateT, ActionT>>,
  private val exitAnyActions: List<StateAction<StateT, StateT, ActionT>>,
  private val enterActions: List<StateAction<SpecificStateT, StateT, ActionT>>,
  private val exitActions: List<StateAction<SpecificStateT, StateT, ActionT>>,
  private val transitions: Map<KClass<out ActionT>, List<GuardedTransition<SpecificStateT, ActionT, StateT, ActionT>>>,
) {

  suspend fun enter(scope: ActionScope<SpecificStateT, StateT, ActionT>) {
    enterAnyActions.forEach { scope.it() }
    enterActions.forEach { scope.it() }
  }

  suspend fun exit(scope: ActionScope<SpecificStateT, StateT, ActionT>) {
    exitAnyActions.forEach { scope.it() }
    exitActions.forEach { scope.it() }
  }

  fun findTransition(scope: TransitionScopeImpl<SpecificStateT, ActionT, StateT, ActionT>):
    StateTransition<SpecificStateT, ActionT, StateT, ActionT> {

    val transitions = transitions
      .filterKeys { type -> type.isInstance(scope.action) }
      .mapNotNull { (_, guarded) -> guarded.single { it.guard(scope) }.transition }

    if (transitions.isEmpty())
      actionError("No transition defined for action (${scope.action}) within state (${scope.state})")

    return transitions.first()
  }
}

private fun actionError(message: String, cause: Throwable? = null): Nothing =
  throw ActionException(message, cause)
