@file:Suppress("FunctionName")

package dev.efemoney.lexiko.statemachine

import dev.efemoney.lexiko.statemachine.internal.StateMachineBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

interface StateMachine<out StateT : Any, in ActionT : Any> {

  val state: StateFlow<StateT>

  val actions: MutableSharedFlow<@UnsafeVariance ActionT>

  suspend fun process(action: ActionT) = actions.emit(action)

  suspend fun cancel()
}

@StateMachineDsl
inline fun <reified StateT : Any, ActionT : Any> CoroutineScope.StateMachine(
  initialState: Any? = null,
  @BuilderInference builder: StateMachineBuilder<StateT, ActionT>.() -> Unit
) = StateMachineBuilder<StateT, ActionT>(
  coroutineScope = this,
  initialState = initialState as StateT?,
).apply(builder).build()

@DelicateCoroutinesApi
@StateMachineDsl
inline fun <reified StateT : Any, reified ActionT : Any> StateMachine(
  initialState: Any? = null,
  @BuilderInference builder: StateMachineBuilder<StateT, ActionT>.() -> Unit
) = GlobalScope.StateMachine(initialState, builder)
