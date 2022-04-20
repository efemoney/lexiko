package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.StateMachineDsl
import kotlinx.coroutines.CoroutineScope

@StateMachineDsl
interface TransitionScope<out SpecificStateT : StateT, out SpecificActionT : ActionT, StateT : Any, ActionT : Any> :
  CoroutineScope {
  val state: SpecificStateT
  val action: SpecificActionT
}

@StateMachineDsl
interface TransitionReturnScope<SpecificStateT : StateT, SpecificActionT : ActionT, StateT : Any, ActionT : Any> :
  TransitionScope<SpecificStateT, SpecificActionT, StateT, ActionT> {

  @StateMachineDsl
  fun <T : StateT> transition(to: T): Return<T> = ReturnT(to)

  @StateMachineDsl
  fun noTransition(): Return<Nothing> = ReturnNothing
}


internal class TransitionScopeImpl<SpecificStateT : StateT, SpecificActionT : ActionT, StateT : Any, ActionT : Any>(
  override val state: SpecificStateT,
  override val action: SpecificActionT,
  coroutineScope: CoroutineScope
) : TransitionReturnScope<SpecificStateT, SpecificActionT, StateT, ActionT>, CoroutineScope by coroutineScope

internal class GuardedTransition<SpecificStateT : StateT, SpecificActionT : ActionT, StateT : Any, ActionT : Any>(
  val guard: StateTransitionGuard<SpecificStateT, SpecificActionT, StateT, ActionT>,
  val transition: StateTransition<SpecificStateT, SpecificActionT, StateT, ActionT>,
)

internal typealias StateTransitionGuard<State, Action, StateT, ActionT> =
  TransitionScope<State, Action, StateT, ActionT>.() -> Boolean

internal typealias StateTransition<State, Action, StateT, ActionT> =
  suspend TransitionReturnScope<State, Action, StateT, ActionT>.() -> Return<StateT>
