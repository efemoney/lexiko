package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.StateMachine
import dev.efemoney.lexiko.statemachine.dsl.ActionScope
import dev.efemoney.lexiko.statemachine.dsl.ReturnNothing
import dev.efemoney.lexiko.statemachine.dsl.ReturnT
import dev.efemoney.lexiko.statemachine.dsl.StateAction
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

sealed interface StateMachineInternal<out StateT : Any, in EventT : Any> : StateMachine<StateT, EventT> {

  val events: MutableSharedFlow<@UnsafeVariance EventT>

  override suspend fun process(event: EventT) = events.emit(event)
}

internal class StateMachineImpl<StateT : Any, EventT : Any>(
  initialState: StateT,
  private val coroutineScope: CoroutineScope,
  private val definitions: Map<KClass<out StateT>, StateDefinition<StateT, StateT, EventT>>,
) : StateMachineInternal<StateT, EventT> {

  override val state = MutableStateFlow(initialState)
  override val events = MutableSharedFlow<EventT>()

  private val actionsJob = events
    .onEach(::handleAction)
    .launchIn(coroutineScope)

  private val initJob = coroutineScope.launch {
    val currentState = state.value
    val currentDefinition = definitionFor(currentState)
    currentDefinition.enter(ActionScopeImpl(currentState, events, coroutineScope))
  }

  override suspend fun cancel() {
    initJob.cancelAndJoin()
    actionsJob.cancelAndJoin()
  }

  private suspend fun handleAction(action: EventT) = coroutineScope {
    val currentState = state.value
    val currentDefinition = definitionFor(currentState)

    val transitionScope = TransitionScopeImpl(currentState, action, coroutineScope)
    val transition = currentDefinition.findTransitionWith(transitionScope)

    currentCoroutineContext().ensureActive() // We have a transition, lets exit current state
    currentDefinition.exit(ActionScopeImpl(currentState, events, coroutineScope))

    currentCoroutineContext().ensureActive() // Then run transition
    val newState = when (val result = transitionScope.transition()) {
      ReturnNothing -> return@coroutineScope
      is ReturnT -> result.state
    }
    val newDefinition = definitionFor(newState)

    state.value = newState

    currentCoroutineContext().ensureActive()
    newDefinition.enter(ActionScopeImpl(newState, events, coroutineScope))
  }

  private fun definitionFor(state: StateT) = definitions.getOrElse(state::class) {
    actionError("Could not find definition for current state ($state)")
  }
}

internal class StateDefinition<SpecificStateT : StateT, StateT : Any, EventT : Any>(
  private val enterAnyActions: List<StateAction<StateT, StateT, EventT>>,
  private val exitAnyActions: List<StateAction<StateT, StateT, EventT>>,
  private val enterActions: List<StateAction<SpecificStateT, StateT, EventT>>,
  private val exitActions: List<StateAction<SpecificStateT, StateT, EventT>>,
  private val transitions: Map<KClass<out EventT>, List<GuardedTransition<SpecificStateT, EventT, StateT, EventT>>>,
) {

  suspend fun enter(scope: ActionScope<SpecificStateT, StateT, EventT>) {
    enterAnyActions.forEach { scope.it() }
    enterActions.forEach { scope.it() }
  }

  suspend fun exit(scope: ActionScope<SpecificStateT, StateT, EventT>) {
    exitAnyActions.forEach { scope.it() }
    exitActions.forEach { scope.it() }
  }

  fun findTransitionWith(scope: TransitionScopeImpl<SpecificStateT, EventT, StateT, EventT>):
    StateTransition<SpecificStateT, EventT, StateT, EventT> {

    val actualTransitions = findSpecificClassTransitions(scope)
      .ifEmpty { findInstanceCheckTransitions(scope) } // Todo: Maybe find by superclass hierarchy?

    if (actualTransitions.isEmpty()) actionError(
      "No transition defined for action '${scope.event}' on state '${scope.state}'"
    )

    if (actualTransitions.size > 1) actionError(
      "Multiple transitions defined for action '${scope.event}' on state '${scope.state}'." +
        " Ensure that only 1 guard condition returns 'true'"
    )

    return actualTransitions.single().transition
  }

  private fun findSpecificClassTransitions(scope: TransitionScopeImpl<SpecificStateT, EventT, StateT, EventT>) =
    transitions[scope.event::class].orEmpty().filter { it.guard.invoke(scope) }

  private fun findInstanceCheckTransitions(scope: TransitionScopeImpl<SpecificStateT, EventT, StateT, EventT>) =
    transitions.filterKeys { it.isInstance(scope.event) }.values.flatten().filter { it.guard.invoke(scope) }
}

private fun actionError(message: String, cause: Throwable? = null): Nothing =
  throw IllegalStateException(message, cause)
