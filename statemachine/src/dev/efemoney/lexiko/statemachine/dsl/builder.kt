@file:Suppress("UNCHECKED_CAST")

import dev.efemoney.lexiko.statemachine.StateMachine
import dev.efemoney.lexiko.statemachine.dsl.Action
import dev.efemoney.lexiko.statemachine.dsl.StateMachineDsl
import dev.efemoney.lexiko.statemachine.dsl.Transition
import dev.efemoney.lexiko.statemachine.dsl.TransitionGuard
import dev.efemoney.lexiko.statemachine.internal.GuardedTransition
import dev.efemoney.lexiko.statemachine.internal.StateDefinition
import dev.efemoney.lexiko.statemachine.internal.StateMachineImpl
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

@StateMachineDsl
class StateMachineBuilder<StateT : Any, EventT : Any>(
  private val coroutineScope: CoroutineScope,
  private val baseType: KClass<StateT>,
  private var initialState: StateT? = null,
) {
  private val enterAnyActions = mutableListOf<Action<StateT, StateT, EventT>>()
  private val exitAnyActions = mutableListOf<Action<StateT, StateT, EventT>>()
  private val anyTransitions =
    mutableMapOf<KClass<out EventT>, MutableList<GuardedTransition<StateT, EventT, StateT, EventT>>>()

  private val definitions = mutableMapOf<KClass<out StateT>, StateDefinition<StateT, StateT, EventT>>()

  @StateMachineDsl
  fun <T : StateT> initialState(state: T) {
    initialState = state
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
    state(baseType) {
      on(eventType, guard, transition)
    }
  }

  // region State DSL
  @StateMachineDsl
  inline fun <reified T : StateT> state(noinline builder: StateBuilder<T, StateT, EventT>.() -> Unit) =
    state(T::class, builder)

  @PublishedApi
  internal fun <T : StateT> state(type: KClass<T>, builder: StateBuilder<T, StateT, EventT>.() -> Unit) {
    if (type in definitions) definitionError("State definition for $type already exists")

    definitions[type] = StateBuilder<T, StateT, EventT>(enterAnyActions, exitAnyActions)
      .apply(builder)
      .build() as StateDefinition<StateT, StateT, EventT>
  }
  // endregion

  // region Nested State DSL
  @StateMachineDsl
  inline fun <reified T : StateT, E : EventT> nestedState(
    @BuilderInference noinline builder: StateMachineBuilder<T, E>.() -> Unit
  ) = nestedState(T::class, builder)

  @PublishedApi
  internal fun <T : StateT, E : EventT> nestedState(
    type: KClass<T>,
    builder: StateMachineBuilder<T, E>.() -> Unit
  ) = nestedState(type, StateMachineBuilder<T, E>(coroutineScope, type).apply(builder).build())

  @StateMachineDsl
  inline fun <reified T : StateT, E : EventT> nestedState(machine: StateMachine<T, E>) =
    nestedState(T::class, machine)

  @PublishedApi
  internal fun <T : StateT, E : EventT> nestedState(type: KClass<T>, machine: StateMachine<T, E>) {

  }
  // endregion

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
  private val enterAnyActions: MutableList<Action<StateT, StateT, EventT>>,
  private val exitAnyActions: MutableList<Action<StateT, StateT, EventT>>,
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
    StateDefinition(enterAnyActions, exitAnyActions, enterActions, exitActions, transitions.toMap())
}

private fun definitionError(message: String, cause: Throwable? = null): Nothing =
  throw IllegalArgumentException(message, cause)
