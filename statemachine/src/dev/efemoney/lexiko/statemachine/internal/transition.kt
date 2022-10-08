package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.dsl.Transition
import dev.efemoney.lexiko.statemachine.dsl.TransitionGuard
import dev.efemoney.lexiko.statemachine.dsl.TransitionReturnScope
import kotlinx.coroutines.CoroutineScope

internal class TransitionScopeImpl<SpecificStateT : StateT, SpecificEventT : EventT, StateT : Any, EventT : Any>(
  override val state: SpecificStateT,
  override val event: SpecificEventT,
  coroutineScope: CoroutineScope
) : TransitionReturnScope<SpecificStateT, SpecificEventT, StateT, EventT>, CoroutineScope by coroutineScope

internal class GuardedTransition<SpecificStateT : StateT, SpecificEventT : EventT, StateT : Any, EventT : Any>(
  val guard: TransitionGuard<SpecificStateT, SpecificEventT, StateT, EventT>,
  val transition: Transition<SpecificStateT, SpecificEventT, StateT, EventT>,
)
