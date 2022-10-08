package dev.efemoney.lexiko.statemachine.dsl

import kotlinx.coroutines.CoroutineScope

@StateMachineDsl
interface TransitionScope<out SpecificStateT : StateT, out SpecificEventT : EventT, StateT : Any, EventT : Any> :
  CoroutineScope {
  val state: SpecificStateT
  val event: SpecificEventT
}

@StateMachineDsl
interface TransitionReturnScope<SpecificStateT : StateT, SpecificEventT : EventT, StateT : Any, EventT : Any> :
  TransitionScope<SpecificStateT, SpecificEventT, StateT, EventT> {

  @StateMachineDsl
  fun <T : StateT> transition(to: T): Return<T> = ReturnT(to)

//  @StateMachineDsl
//  fun <T : StateT> transition(to: T): Return<T> = ReturnT(to)

  @StateMachineDsl
  fun noTransition(): Return<Nothing> = ReturnNothing
}

sealed interface Return<out T>

internal class ReturnT<T>(val state: T) : Return<T>

internal object ReturnNothing : Return<Nothing>

internal typealias TransitionGuard<State, Event, StateT, EventT> =
  TransitionScope<State, Event, StateT, EventT>.() -> Boolean

internal typealias Transition<State, Event, StateT, EventT> =
  suspend TransitionReturnScope<State, Event, StateT, EventT>.() -> Return<StateT>
