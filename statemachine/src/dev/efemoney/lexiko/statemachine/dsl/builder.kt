@file:Suppress("UNCHECKED_CAST")

import dev.efemoney.lexiko.statemachine.StateMachine
import dev.efemoney.lexiko.statemachine.dsl.Action
import dev.efemoney.lexiko.statemachine.dsl.StateMachineDsl
import dev.efemoney.lexiko.statemachine.dsl.Transition
import dev.efemoney.lexiko.statemachine.dsl.TransitionGuard
import dev.efemoney.lexiko.statemachine.internal.GuardedTransition
import dev.efemoney.lexiko.statemachine.internal.StateDefinition
import dev.efemoney.lexiko.statemachine.internal.StateMachineImpl
import kotlin.reflect.KClass

@StateMachineDsl
class StateMachineBuilder<StateT : Any, EventT : Any> internal constructor(
  private val baseType: KClass<StateT>,
  initialState: StateT? = null,
) {
  private var computeInitialState = { initialState }
  private val enterAnyActions = mutableListOf<Action<StateT, StateT, EventT>>()
  private val exitAnyActions = mutableListOf<Action<StateT, StateT, EventT>>()
  private val anyTransitions =
    mutableMapOf<KClass<out EventT>, MutableList<GuardedTransition<StateT, EventT, StateT, EventT>>>()

  private val definitions = mutableMapOf<KClass<out StateT>, StateDefinition<StateT, StateT, EventT>>()

  @StateMachineDsl
  fun <T : StateT> initialState(state: T) {
    computeInitialState = { state }
  }

  @StateMachineDsl
  inline fun <reified T : StateT> initialState() = initialNestedState(T::class)

  @PublishedApi
  internal fun <T : StateT> initialNestedState(type: KClass<T>) {
    computeInitialState = { definitions[type]?.nested?.state?.value }
  }

  @StateMachineDsl
  fun onEnterAny(action: Action<StateT, StateT, EventT>) {
    enterAnyActions += action
  }

  @StateMachineDsl
  fun onExitAny(action: Action<StateT, StateT, EventT>) {
    exitAnyActions += action
  }

  @StateMachineDsl
  inline fun <reified T : EventT> onAny(
    noinline guard: TransitionGuard<StateT, T, StateT, EventT> = { true },
    noinline transition: Transition<StateT, T, StateT, EventT>,
  ) = onAny(T::class, guard, transition)

  @StateMachineDsl
  @PublishedApi
  internal fun <T : EventT> onAny(
    eventType: KClass<T>,
    guard: TransitionGuard<StateT, T, StateT, EventT>,
    transition: Transition<StateT, T, StateT, EventT>,
  ) {
    state(baseType, null) {
      on(eventType, guard, transition)
    }
  }

  // region State DSL
  @StateMachineDsl
  inline fun <reified T : StateT> state(noinline builder: StateBuilder<T, StateT, EventT>.() -> Unit) =
    state(T::class, null, builder)

  @StateMachineDsl
  inline fun <reified T : StateT> state(
    nested: StateMachine<T, EventT>,
    noinline builder: StateBuilder<T, StateT, EventT>.() -> Unit
  ) = state(T::class, nested as StateMachineImpl<StateT, EventT>, builder)

  @PublishedApi
  internal fun <T : StateT> state(
    type: KClass<T>,
    nested: StateMachineImpl<StateT, EventT>?,
    builder: StateBuilder<T, StateT, EventT>.() -> Unit
  ) {
    if (type in definitions) definitionError("State definition for $type already exists")

    definitions[type] = StateBuilder<T, StateT, EventT>(enterAnyActions, exitAnyActions, nested)
      .apply(builder)
      .build() as StateDefinition<StateT, StateT, EventT>
  }
  // endregion

  @PublishedApi
  internal fun build() = StateMachineImpl(
    initialState = checkNotNull(computeInitialState()) { "Cannot create a StateMachine without an initial state" },
    definitions = definitions.toMap(),
  )
}

@StateMachineDsl
class StateBuilder<SpecificStateT : StateT, StateT : Any, EventT : Any> internal constructor(
  private val enterAnyActions: MutableList<Action<StateT, StateT, EventT>>,
  private val exitAnyActions: MutableList<Action<StateT, StateT, EventT>>,
  private val nested: StateMachineImpl<StateT, EventT>? = null,
) {
  private val enterActions = mutableListOf<Action<SpecificStateT, StateT, EventT>>()
  private val exitActions = mutableListOf<Action<SpecificStateT, StateT, EventT>>()
  private val transitions =
    mutableMapOf<KClass<out EventT>, MutableList<GuardedTransition<SpecificStateT, EventT, StateT, EventT>>>()

  @StateMachineDsl
  fun onEnter(action: Action<SpecificStateT, StateT, EventT>) {
    enterActions += action
  }

  @StateMachineDsl
  fun onExit(action: Action<SpecificStateT, StateT, EventT>) {
    exitActions += action
  }

  @StateMachineDsl
  inline fun <reified T : EventT> on(
    noinline guard: TransitionGuard<SpecificStateT, T, StateT, EventT> = { true },
    noinline transition: Transition<SpecificStateT, T, StateT, EventT>,
  ) = on(T::class, guard, transition)

  @StateMachineDsl
  @PublishedApi
  internal fun <T : EventT> on(
    type: KClass<T>,
    guard: TransitionGuard<SpecificStateT, T, StateT, EventT>,
    transition: Transition<SpecificStateT, T, StateT, EventT>,
  ) {
    transitions
      .getOrPut(type, ::mutableListOf)
      .add(GuardedTransition(guard, transition) as GuardedTransition<SpecificStateT, EventT, StateT, EventT>)
  }

  internal fun build() =
    StateDefinition(enterAnyActions, exitAnyActions, enterActions, exitActions, transitions.toMap(), nested)
}

private fun definitionError(message: String, cause: Throwable? = null): Nothing =
  throw IllegalArgumentException(message, cause)
