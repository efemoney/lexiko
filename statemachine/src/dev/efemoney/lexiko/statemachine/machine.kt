@file:Suppress("FunctionName")

package dev.efemoney.lexiko.statemachine

import dev.efemoney.lexiko.statemachine.internal.StateMachineBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow

interface StateMachine<out StateT : Any, in ActionT : Any> {

  val state: StateFlow<StateT>

  suspend fun process(action: ActionT): Boolean
}

inline fun <reified StateT : Any, reified ActionT : Any> CoroutineScope.StateMachineOf(
  initialState: Any? = null,
  @BuilderInference builder: StateMachineBuilder<StateT, ActionT>.() -> Unit
) = StateMachineBuilder<StateT, ActionT>(initialState as StateT?, this).apply(builder).build()

@DelicateCoroutinesApi
inline fun <reified StateT : Any, reified ActionT : Any> StateMachineOf(
  initialState: Any? = null,
  @BuilderInference builder: StateMachineBuilder<StateT, ActionT>.() -> Unit
) = GlobalScope.StateMachineOf(initialState, builder)
