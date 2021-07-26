@file:Suppress("FunctionName")

package dev.efemoney.lexiko.statemachine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface StateMachine<StateT : Any, ActionT : Any> {
  val state: StateT
  suspend fun process(action: ActionT): Boolean
}

inline fun <reified StateT : Any, reified ActionT : Any> StateMachineOf(
  initialState: StateT? = null,
  @BuilderInference configure: StateMachineBuilder<StateT, ActionT>.() -> Unit
): StateMachine<StateT, ActionT> = StateMachineBuilder<StateT, ActionT>(initialState).apply(configure).build()

// <editor-fold desc="This section contains DSL classes">

private typealias StateTransitionAction<T> =
  suspend StateTransitionContext<T>.() -> Unit

private typealias StateTransitionCoroutineAction<T, ActionT> =
  suspend StateTransitionCoroutineContext<T>.(action: ActionT) -> Unit

@DslMarker
internal annotation class Dsl

@Dsl
class StateMachineBuilder<StateT : Any, ActionT : Any>(
  private var initialState: StateT? = null,
) {

  @Dsl
  fun initialState(state: StateT) {
    initialState = state
  }

  @Dsl
  inline fun <reified T : StateT> state(noinline builder: StateTransitionBuilder<T, ActionT>.() -> Unit) =
    state(typeOf<T>(), builder)

  @Dsl
  fun <T : StateT> state(type: KType, builder: StateTransitionBuilder<T, ActionT>.() -> Unit) =
    StateTransitionBuilder<T, ActionT>(type).builder()

  fun build(): StateMachine<StateT, ActionT> = StateMachineImpl(initialState!!)
}

@Dsl
class StateTransitionBuilder<SpecificStateT : Any, ActionT : Any>(private val type: KType) {

  private val actionRoutines = mutableMapOf<KType, StateTransitionCoroutineAction<SpecificStateT, *>>()
  private val enterActions = mutableListOf<StateTransitionAction<SpecificStateT>>()
  private val exitActions = mutableListOf<StateTransitionAction<SpecificStateT>>()

  @Dsl
  inline fun <reified T : ActionT> on(noinline action: StateTransitionCoroutineAction<SpecificStateT, T>) =
    on(typeOf<T>(), action)

  @Dsl
  fun <T : ActionT> on(type: KType, action: StateTransitionCoroutineAction<SpecificStateT, T>) {
    actionRoutines[type] = action
  }

  @Dsl
  fun onEnter(action: StateTransitionAction<SpecificStateT>) {
    enterActions += action
  }

  @Dsl
  fun onExit(action: StateTransitionAction<SpecificStateT>) {
    exitActions += action
  }
}

@Dsl
interface StateTransitionContext<StateT : Any> {
  val state: StateT
}

@Dsl
interface StateTransitionCoroutineContext<StateT : Any> : StateTransitionContext<StateT>, CoroutineScope

@Dsl
class StateTransitionContextImpl<StateT : Any>(
  override val state: StateT,
  coroutineScope: CoroutineScope
) : StateTransitionCoroutineContext<StateT>, CoroutineScope by coroutineScope

internal class StateMachineImpl<StateT : Any, ActionT : Any>(
  initialState: StateT,
  operationContext: CoroutineContext = EmptyCoroutineContext,
) : StateMachine<StateT, ActionT>, CoroutineScope by CoroutineScope(operationContext) {

  private val actions = MutableSharedFlow<ActionT>()
  private val internalState = MutableStateFlow(initialState)

  override val state by internalState.asStateFlow()

  init {
    internalState
      .onEach { println("new state: $it") }
      .launchIn(this)

    actions
      .onEach { println("new action: $it") }
      .launchIn(this)
  }

  override suspend fun process(action: ActionT) = runCatching { actions.emit(action) }.isSuccess
}

internal fun interface Matcher<T> {
  fun matches(state: Any): Boolean
}

// </editor-fold>
