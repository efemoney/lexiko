package dev.efemoney.lexiko.statemachine.dsl

import StateBuilder
import StateMachineBuilder

@DslMarker
internal annotation class StateMachineDsl

@StateMachineDsl
inline fun <
  reified T : EventT,
  EventT : Enum<*>,
  StateT : Any,
  > StateMachineBuilder<StateT, EventT>.onAny(
  enum: T,
  noinline transition: Transition<StateT, T, StateT, EventT>,
) {
  onAny(
    eventType = T::class,
    guard = { event == enum },
    transition = transition,
  )
}

@StateMachineDsl
inline fun <
  reified T : EventT,
  SpecificStateT : StateT,
  EventT : Enum<*>,
  StateT : Any,
  > StateBuilder<SpecificStateT, StateT, EventT>.on(
  enum: T,
  noinline transition: Transition<SpecificStateT, T, StateT, EventT>,
) {
  on(
    type = T::class,
    guard = { event == enum },
    transition = transition,
  )
}
