@file:Suppress("FunctionName")

package dev.efemoney.lexiko.statemachine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface StateMachine<StateT : Any, ActionT : Any> {
  val state: StateT
  fun process(action: ActionT): Boolean
}

inline fun <reified StateT : Any, reified ActionT : Any> StateMachineOf(
  initialState: StateT? = null,
  @BuilderInference configure: StateMachineBuilder<StateT, ActionT>.() -> Unit
): StateMachine<StateT, ActionT> = StateMachineBuilder<StateT, ActionT>(initialState).apply(configure).build()

// <editor-fold desc="This section contains DSL classes">

@DslMarker
internal annotation class Dsl

@Dsl
class StateMachineBuilder<StateT : Any, ActionT : Any>(
  private var initialState: StateT? = null,
) {

  fun initialState(state: StateT) {
    initialState = state
  }

  fun <T : StateT> state(type: KType, builder: StateTransitionBuilder<T, ActionT>.() -> Unit) =
    StateTransitionBuilder<T, ActionT>(type).builder()

  inline fun <reified T : StateT> state(noinline builder: StateTransitionBuilder<T, ActionT>.() -> Unit) =
    state(typeOf<T>(), builder)

  fun build(): StateMachine<StateT, ActionT> = StateMachineImpl(initialState!!)
}

@Dsl
class StateTransitionBuilder<SpecificStateT : Any, ActionT : Any>(private val specificStateType: KType) {

  fun <T : ActionT> on(type: KType, action: suspend StateTransitionContext<SpecificStateT>.(action: T) -> Unit) {

  }

  inline fun <reified T : ActionT> on(noinline action: suspend StateTransitionContext<SpecificStateT>.(action: T) -> Unit) =
    on(typeOf<T>(), action)
}

@Dsl
class StateTransitionContext<StateT : Any>(val state: StateT, coroutineScope: CoroutineScope) :
  CoroutineScope by coroutineScope

internal class StateMachineImpl<StateT : Any, ActionT : Any>(
  initialState: StateT,
  operationContext: CoroutineContext = EmptyCoroutineContext,
) : StateMachine<StateT, ActionT>, CoroutineScope by CoroutineScope(operationContext) {

  private val internalState = MutableStateFlow(initialState)

  override val state by internalState.asStateFlow()

  init {
    internalState
      .onEach { print("new state: ") }
      .launchIn(this)
  }

  override fun process(action: ActionT) = TODO()
}

internal fun interface Matcher<T> {
  fun matches(state: Any): Boolean
}

// </editor-fold>
