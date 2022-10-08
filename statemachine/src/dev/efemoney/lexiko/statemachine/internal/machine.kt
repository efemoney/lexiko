package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.StateMachine
import dev.efemoney.lexiko.statemachine.dsl.Action
import dev.efemoney.lexiko.statemachine.dsl.ActionScope
import dev.efemoney.lexiko.statemachine.dsl.ReturnMachine
import dev.efemoney.lexiko.statemachine.dsl.ReturnNothing
import dev.efemoney.lexiko.statemachine.dsl.ReturnT
import dev.efemoney.lexiko.statemachine.dsl.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

sealed interface StateMachineInternal<out StateT : Any, in EventT : Any> : StateMachine<StateT, EventT> {

  val events: MutableSharedFlow<Event<@UnsafeVariance EventT>>

  override suspend fun process(event: EventT) = events.emit(Event.RunAction(event))
}

internal class StateMachineImpl<StateT : Any, EventT : Any>(
  initialState: StateT,
  private val definitions: Map<KClass<out StateT>, StateDefinition<StateT, StateT, EventT>>,
) : StateMachineInternal<StateT, EventT> {

  private lateinit var job: Job

  override val state = MutableStateFlow(initialState)
  override val events = MutableSharedFlow<Event<EventT>>()

  override suspend fun cancel() = job.cancelAndJoin()

  internal fun runIn(scope: CoroutineScope) {
    job = scope.launch {
      events.collect {
        when (it) {
          is Event.RunAction -> handle(it.event)
          is Event.RunEnterAction -> coroutineScope {
            val currentState = state.value
            val currentDefinition = definitionFor(currentState)
            currentDefinition.enter(ActionScopeImpl(currentState, events, this))
          }
        }
      }
    }
    scope.launch {
      events.emit(Event.RunEnterAction)
    }
  }

  private suspend fun handle(event: EventT) = coroutineScope {
    val currentState = state.value
    val currentDefinition = definitionFor(currentState)

    val transitionScope = TransitionScopeImpl(currentState, event, this)
    val transition = currentDefinition.findTransitionWith(transitionScope)

    currentCoroutineContext().ensureActive() // We have a transition, lets exit current state
    currentDefinition.exit(ActionScopeImpl(currentState, events, this))

    currentCoroutineContext().ensureActive() // Then run transition
    val newState = when (val result = transitionScope.transition()) {
      ReturnNothing -> return@coroutineScope
      is ReturnT -> result.state
      is ReturnMachine -> TODO()
    }
    val newDefinition = definitionFor(newState)

    state.value = newState

    currentCoroutineContext().ensureActive()
    newDefinition.enter(ActionScopeImpl(newState, events, this))
  }

  private fun definitionFor(state: StateT) =
    definitions[state::class] ?: definitions.filterKeys { it.isInstance(state) }.let {
      if (it.isEmpty()) actionError("Could not find state definition for state [$state]")
      if (it.size > 1) actionError("Multiple state definitions found for state [$state]: found definitions [${it.keys.joinToString()}]")
      it.values.first()
    }
}

internal class StateDefinition<SpecificStateT : StateT, StateT : Any, EventT : Any>(
  private val enterAnyActions: List<Action<StateT, StateT, EventT>>,
  private val exitAnyActions: List<Action<StateT, StateT, EventT>>,
  private val enterActions: List<Action<SpecificStateT, StateT, EventT>>,
  private val exitActions: List<Action<SpecificStateT, StateT, EventT>>,
  private val transitions: Map<KClass<out EventT>, List<GuardedTransition<SpecificStateT, EventT, StateT, EventT>>>,
  internal val nested: StateMachineImpl<StateT, EventT>? = null,
) {

  suspend fun enter(scope: ActionScope<SpecificStateT, StateT, EventT>) {
    coroutineScope { // Run all global actions
      for (action in enterAnyActions) launch { scope.action() }
    }
    coroutineScope { // Then run all state actions
      for (action in enterActions) launch { scope.action() }
    }
  }

  suspend fun exit(scope: ActionScope<SpecificStateT, StateT, EventT>) {
    coroutineScope { // Run all global actions
      for (action in exitAnyActions) launch { scope.action() }
    }
    coroutineScope { // Then run all state actions
      for (action in exitActions) launch { scope.action() }
    }
  }

  fun findTransitionWith(scope: TransitionScopeImpl<SpecificStateT, EventT, StateT, EventT>):
    Transition<SpecificStateT, EventT, StateT, EventT> {

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

sealed interface Event<out T : Any> {
  data object RunEnterAction : Event<Nothing>
  data class RunAction<T : Any>(val event: T) : Event<T>
}

private fun actionError(message: String, cause: Throwable? = null): Nothing =
  throw IllegalStateException(message, cause)
