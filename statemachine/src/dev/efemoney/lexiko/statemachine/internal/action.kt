package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.StateMachineDsl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@StateMachineDsl
interface ActionScope<out SpecificStateT : StateT, StateT : Any, in ActionT : Any> : CoroutineScope {

  /** The state that was just entered/exited */
  val state: SpecificStateT

  /** Launch a coroutine to process an [action] as a side effect of [this][ActionScope] enter/exit action */
  @StateMachineDsl
  fun emit(action: ActionT)
}

internal class ActionScopeImpl<SpecificStateT : StateT, StateT : Any, ActionT : Any>(
  override val state: SpecificStateT,
  private val actions: MutableSharedFlow<ActionT>,
  coroutineScope: CoroutineScope,
) : ActionScope<SpecificStateT, StateT, ActionT>, CoroutineScope by coroutineScope {

  override fun emit(action: ActionT) {
    launch { actions.emit(action) }
  }
}

internal typealias StateAction<State, StateT, ActionT> =
  suspend ActionScope<State, StateT, ActionT>.() -> Unit
