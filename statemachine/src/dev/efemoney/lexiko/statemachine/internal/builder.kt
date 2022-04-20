@file:Suppress("UNCHECKED_CAST")

package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.DefinitionException
import dev.efemoney.lexiko.statemachine.StateMachine
import dev.efemoney.lexiko.statemachine.StateMachineDsl
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

@StateMachineDsl
class StateMachineBuilder<StateT : Any, ActionT : Any>(
  private val coroutineScope: CoroutineScope,
  private var initialState: StateT? = null,
) {
  private val enterAnyActions = mutableListOf<StateAction<StateT, StateT, ActionT>>()
  private val exitAnyActions = mutableListOf<StateAction<StateT, StateT, ActionT>>()
  private val definitions = mutableMapOf<KClass<out StateT>, StateDefinition<StateT, StateT, ActionT>>()

  @StateMachineDsl
  fun <T : StateT> initialState(state: T) {
    initialState = state
  }

  @StateMachineDsl
  fun onEnterAny(action: StateAction<StateT, StateT, ActionT>) {
    enterAnyActions += action
  }

  @StateMachineDsl
  fun onExitAny(action: StateAction<StateT, StateT, ActionT>) {
    exitAnyActions += action
  }

  @StateMachineDsl
  inline fun <reified T : StateT> state(noinline builder: StateBuilder<T, StateT, ActionT>.() -> Unit) =
    state(T::class, builder)

  @StateMachineDsl
  @PublishedApi
  internal fun <T : StateT> state(type: KClass<T>, builder: StateBuilder<T, StateT, ActionT>.() -> Unit) {
    if (type in definitions) definitionError("State definition for $type already exists")

    definitions[type] = StateBuilder<T, StateT, ActionT>(enterAnyActions, exitAnyActions)
      .apply(builder)
      .build() as StateDefinition<StateT, StateT, ActionT>
  }

  @PublishedApi
  internal fun build(): StateMachine<StateT, ActionT> =
    StateMachineImpl(
      initialState = checkNotNull(initialState) { "Cannot create a StateMachine without an initial state" },
      coroutineScope = coroutineScope,
      definitions = definitions.toMap(),
    )
}

@StateMachineDsl
class StateBuilder<SpecificStateT : StateT, StateT : Any, ActionT : Any>(
  private val enterAnyActions: MutableList<StateAction<StateT, StateT, ActionT>>,
  private val exitAnyActions: MutableList<StateAction<StateT, StateT, ActionT>>,
) {
  private val enterActions = mutableListOf<StateAction<SpecificStateT, StateT, ActionT>>()
  private val exitActions = mutableListOf<StateAction<SpecificStateT, StateT, ActionT>>()
  private val transitions =
    mutableMapOf<KClass<out ActionT>, MutableList<GuardedTransition<SpecificStateT, ActionT, StateT, ActionT>>>()

  @StateMachineDsl
  fun onEnter(action: StateAction<SpecificStateT, StateT, ActionT>) {
    enterActions += action
  }

  @StateMachineDsl
  fun onExit(action: StateAction<SpecificStateT, StateT, ActionT>) {
    exitActions += action
  }

  @StateMachineDsl
  inline fun <reified T : ActionT> on(
    noinline guard: StateTransitionGuard<SpecificStateT, T, StateT, ActionT> = { true },
    noinline transition: StateTransition<SpecificStateT, T, StateT, ActionT>,
  ) = on(T::class, guard, transition)

  @StateMachineDsl
  @PublishedApi
  internal fun <T : ActionT> on(
    type: KClass<T>,
    guard: StateTransitionGuard<SpecificStateT, T, StateT, ActionT>,
    transition: StateTransition<SpecificStateT, T, StateT, ActionT>,
  ) {
    transitions
      .getOrPut(type, ::mutableListOf)
      .add(GuardedTransition(guard, transition) as GuardedTransition<SpecificStateT, ActionT, StateT, ActionT>)
  }

  internal fun build() =
    StateDefinition(enterAnyActions, exitAnyActions, enterActions, exitActions, transitions.toMap())
}

private fun definitionError(message: String, cause: Throwable? = null): Nothing =
  throw DefinitionException(message, cause)
