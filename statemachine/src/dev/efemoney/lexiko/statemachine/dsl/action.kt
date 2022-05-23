package dev.efemoney.lexiko.statemachine.dsl

import kotlinx.coroutines.CoroutineScope

@StateMachineDsl
interface ActionScope<out SpecificStateT : StateT, StateT : Any, in EventT : Any> : CoroutineScope {

  /** The state that was just entered/exited */
  val state: SpecificStateT

  /** Launch a coroutine to process an [event] as a side effect of [this][ActionScope] enter/exit action */
  @StateMachineDsl
  fun emit(event: EventT)
}

internal typealias StateAction<State, StateT, EventT> =
  suspend ActionScope<State, StateT, EventT>.() -> Unit
