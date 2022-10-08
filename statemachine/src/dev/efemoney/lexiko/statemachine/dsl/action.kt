package dev.efemoney.lexiko.statemachine.dsl

@StateMachineDsl
interface ActionScope<out SpecificStateT : StateT, StateT : Any, in EventT : Any> {

  /** The state that was just entered/exited */
  val state: SpecificStateT

  /** Emit an [event] as a side effect of [this][ActionScope] enter/exit action */
  @StateMachineDsl
  fun emit(event: EventT)
}

internal typealias Action<State, StateT, EventT> =
  suspend ActionScope<State, StateT, EventT>.() -> Unit
