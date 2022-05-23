package dev.efemoney.lexiko.statemachine.dsl

import StateBuilder
import dev.efemoney.lexiko.statemachine.internal.StateTransition
import dev.efemoney.lexiko.statemachine.internal.StateTransitionGuard

@DslMarker
annotation class StateMachineDsl

@StateMachineDsl
inline fun <
  reified T : EventT,
  SpecificStateT : StateT,
  EventT : Enum<*>,
  StateT : Any,
  > StateBuilder<SpecificStateT, StateT, EventT>.on(
  enum: T,
  noinline transition: StateTransition<SpecificStateT, T, StateT, EventT>,
) {
  on(
    type = T::class,
    guard = { event == enum },
    transition = transition,
  )
}

@OverloadResolutionByLambdaReturnType
@StateMachineDsl
inline fun <
  reified T : EventT,
  SpecificStateT : StateT,
  EventT : Any,
  StateT : Any,
  > StateBuilder<SpecificStateT, StateT, EventT>.on(
  noinline guard: StateTransitionGuard<SpecificStateT, T, StateT, EventT> = { true },
  crossinline notTransition: StateNoTransition<SpecificStateT, T, StateT, EventT>,
) {
  on(
    guard = guard,
    transition = { notTransition(); noTransition() }
  )
}

internal typealias StateNoTransition<State, Event, StateT, EventT> =
  suspend TransitionReturnScope<State, Event, StateT, EventT>.() -> Unit
