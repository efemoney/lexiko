package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.dsl.Return
import dev.efemoney.lexiko.statemachine.dsl.TransitionReturnScope
import dev.efemoney.lexiko.statemachine.dsl.TransitionScope
import kotlinx.coroutines.CoroutineScope

internal class TransitionScopeImpl<SpecificStateT : StateT, SpecificEventT : EventT, StateT : Any, EventT : Any>(
  override val state: SpecificStateT,
  override val event: SpecificEventT,
  coroutineScope: CoroutineScope
) : TransitionReturnScope<SpecificStateT, SpecificEventT, StateT, EventT>, CoroutineScope by coroutineScope

internal class GuardedTransition<SpecificStateT : StateT, SpecificEventT : EventT, StateT : Any, EventT : Any>(
  val guard: StateTransitionGuard<SpecificStateT, SpecificEventT, StateT, EventT>,
  val transition: StateTransition<SpecificStateT, SpecificEventT, StateT, EventT>,
)

internal typealias StateTransitionGuard<State, Event, StateT, EventT> =
  TransitionScope<State, Event, StateT, EventT>.() -> Boolean

internal typealias StateTransition<State, Event, StateT, EventT> =
  suspend TransitionReturnScope<State, Event, StateT, EventT>.() -> Return<StateT>
