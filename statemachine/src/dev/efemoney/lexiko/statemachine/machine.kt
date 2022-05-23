@file:Suppress("FunctionName")

package dev.efemoney.lexiko.statemachine

import kotlinx.coroutines.flow.StateFlow

interface StateMachine<out StateT : Any, in EventT : Any> {

  val state: StateFlow<StateT>

  suspend fun process(event: EventT)

  suspend fun cancel()
}
