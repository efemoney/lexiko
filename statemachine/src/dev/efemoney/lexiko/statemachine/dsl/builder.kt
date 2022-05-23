@file:Suppress("UNCHECKED_CAST")

import dev.efemoney.lexiko.statemachine.StateMachine
import dev.efemoney.lexiko.statemachine.dsl.StateAction
import dev.efemoney.lexiko.statemachine.dsl.StateMachineDsl
import dev.efemoney.lexiko.statemachine.internal.*
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

@StateMachineDsl
class StateMachineBuilder<StateT : Any, EventT : Any>(
  private val coroutineScope: CoroutineScope,
  private var initialState: StateT? = null,
) {
  private val enterAnyActions = mutableListOf<StateAction<StateT, StateT, EventT>>()
  private val exitAnyActions = mutableListOf<StateAction<StateT, StateT, EventT>>()
  private val definitions = mutableMapOf<KClass<out StateT>, StateDefinition<StateT, StateT, EventT>>()

  @StateMachineDsl
  fun <T : StateT> initialState(state: T) {
    initialState = state
  }

  @StateMachineDsl
  fun onEnterAny(action: StateAction<StateT, StateT, EventT>) {
    enterAnyActions += action
  }

  @StateMachineDsl
  fun onExitAny(action: StateAction<StateT, StateT, EventT>) {
    exitAnyActions += action
  }

  @StateMachineDsl
  inline fun <reified T : StateT> state(noinline builder: StateBuilder<T, StateT, EventT>.() -> Unit) =
    state(T::class, builder)

  @StateMachineDsl
  @PublishedApi
  internal fun <T : StateT> state(type: KClass<T>, builder: StateBuilder<T, StateT, EventT>.() -> Unit) {
    if (type in definitions) definitionError("State definition for $type already exists")

    definitions[type] = StateBuilder<T, StateT, EventT>(enterAnyActions, exitAnyActions)
      .apply(builder)
      .build() as StateDefinition<StateT, StateT, EventT>
  }

  @PublishedApi
  internal fun build(): StateMachine<StateT, EventT> =
    StateMachineImpl(
      initialState = checkNotNull(initialState) { "Cannot create a StateMachine without an initial state" },
      coroutineScope = coroutineScope,
      definitions = definitions.toMap(),
    )
}

@StateMachineDsl
class StateBuilder<SpecificStateT : StateT, StateT : Any, EventT : Any>(
  private val enterAnyActions: MutableList<StateAction<StateT, StateT, EventT>>,
  private val exitAnyActions: MutableList<StateAction<StateT, StateT, EventT>>,
) {
  private val enterActions = mutableListOf<StateAction<SpecificStateT, StateT, EventT>>()
  private val exitActions = mutableListOf<StateAction<SpecificStateT, StateT, EventT>>()
  private val transitions =
    mutableMapOf<KClass<out EventT>, MutableList<GuardedTransition<SpecificStateT, EventT, StateT, EventT>>>()

  @StateMachineDsl
  fun onEnter(action: StateAction<SpecificStateT, StateT, EventT>) {
    enterActions += action
  }

  @StateMachineDsl
  fun onExit(action: StateAction<SpecificStateT, StateT, EventT>) {
    exitActions += action
  }

  @OverloadResolutionByLambdaReturnType
  @StateMachineDsl
  inline fun <reified T : EventT> on(
    noinline guard: StateTransitionGuard<SpecificStateT, T, StateT, EventT> = { true },
    noinline transition: StateTransition<SpecificStateT, T, StateT, EventT>,
  ) = on(T::class, guard, transition)

  @StateMachineDsl
  @PublishedApi
  internal fun <T : EventT> on(
    type: KClass<T>,
    guard: StateTransitionGuard<SpecificStateT, T, StateT, EventT>,
    transition: StateTransition<SpecificStateT, T, StateT, EventT>,
  ) {
    transitions
      .getOrPut(type, ::mutableListOf)
      .add(GuardedTransition(guard, transition) as GuardedTransition<SpecificStateT, EventT, StateT, EventT>)
  }

  internal fun build() =
    StateDefinition(enterAnyActions, exitAnyActions, enterActions, exitActions, transitions.toMap())
}

private fun definitionError(message: String, cause: Throwable? = null): Nothing =
  throw IllegalArgumentException(message, cause)
