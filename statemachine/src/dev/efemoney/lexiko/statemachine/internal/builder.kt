package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.reflect.KClass

@StateMachineDsl
class StateMachineBuilder<StateT : Any, ActionT : Any>(
  private var initialState: StateT? = null,
  private val coroutineScope: CoroutineScope,
) {

  private val definitions = mutableMapOf<KClass<out StateT>, StateDefinition<StateT, StateT, ActionT>>()

  @StateMachineDsl
  fun <T : StateT> initialState(state: T) {
    initialState = state
  }

  @StateMachineDsl
  fun <T : StateT> state(kls: KClass<T>, builder: StateDefinitionBuilder<T, StateT, ActionT>.() -> Unit) {
    @Suppress("UNCHECKED_CAST")
    definitions[kls] = StateDefinitionBuilder<T, StateT, ActionT>().apply(builder).build()
      as StateDefinition<StateT, StateT, ActionT>
  }

  @StateMachineDsl
  inline fun <reified T : StateT> state(noinline builder: StateDefinitionBuilder<T, StateT, ActionT>.() -> Unit) =
    state(T::class, builder)

  @PublishedApi
  internal fun build(): StateMachine<StateT, ActionT> =
    StateMachineImpl(checkNotNull(initialState), coroutineScope, definitions.toMap())
}


internal class StateDefinition<SpecificStateT : StateT, StateT : Any, ActionT : Any>(
  private val enterActions: List<StateAction<SpecificStateT, StateT, ActionT>>,
  private val exitActions: List<StateAction<SpecificStateT, StateT, ActionT>>,
  internal val transitions: Map<KClass<out ActionT>, StateTransition<SpecificStateT, ActionT, StateT, ActionT>>,
) {
  suspend fun exit(scope: StateActionScope<SpecificStateT, StateT, ActionT>) = exitActions.forEach { scope.it() }
  suspend fun enter(scope: StateActionScope<SpecificStateT, StateT, ActionT>) = enterActions.forEach { scope.it() }
}

@StateMachineDsl
class StateDefinitionBuilder<SpecificStateT : StateT, StateT : Any, ActionT : Any> {
  private val enterActions = mutableListOf<StateAction<SpecificStateT, StateT, ActionT>>()
  private val exitActions = mutableListOf<StateAction<SpecificStateT, StateT, ActionT>>()
  private val transitions =
    mutableMapOf<KClass<out ActionT>, StateTransition<SpecificStateT, ActionT, StateT, ActionT>>()

  @StateMachineDsl
  fun onEnter(action: StateAction<SpecificStateT, StateT, ActionT>) {
    enterActions += action
  }

  @StateMachineDsl
  fun onExit(action: StateAction<SpecificStateT, StateT, ActionT>) {
    exitActions += action
  }

  @StateMachineDsl
  inline fun <reified T : ActionT> on(noinline action: StateTransition<SpecificStateT, T, StateT, ActionT>) =
    on(T::class, action)

  @StateMachineDsl
  fun <T : ActionT> on(type: KClass<T>, action: StateTransition<SpecificStateT, T, StateT, ActionT>) {
    @Suppress("UNCHECKED_CAST")
    transitions[type] = action as StateTransition<SpecificStateT, ActionT, StateT, ActionT>
  }

  internal fun build() = StateDefinition(enterActions.toList(), exitActions.toList(), transitions.toMap())
}


private typealias StateAction<State, StateT, ActionT> =
  suspend StateActionScope<State, StateT, ActionT>.() -> Unit

@StateMachineDsl
class StateActionScope<SpecificStateT : StateT, StateT : Any, ActionT: Any> internal constructor(
  val state: SpecificStateT,
  private val actions: MutableSharedFlow<ActionT>,
  coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope


private typealias StateTransition<State, Action, StateT, ActionT> =
  suspend StateTransitionScope<State, Action, StateT, ActionT>.() -> Return<out StateT>

@StateMachineDsl
class StateTransitionScope<SpecificStateT : StateT, SpecificActionT : ActionT, StateT : Any, ActionT : Any> internal constructor(
  val state: SpecificStateT,
  val action: SpecificActionT,
  coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

  @StateMachineDsl
  fun <T : StateT> transition(to: T): Return<T> = ReturnT(to)

  @StateMachineDsl
  fun noTransition(): Return<Nothing> = ReturnNothing
}
