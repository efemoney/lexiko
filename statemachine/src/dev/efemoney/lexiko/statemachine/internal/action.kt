package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.dsl.ActionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

internal class ActionScopeImpl<SpecificStateT : StateT, StateT : Any, EventT : Any>(
  override val state: SpecificStateT,
  private val events: MutableSharedFlow<EventT>,
  coroutineScope: CoroutineScope,
) : ActionScope<SpecificStateT, StateT, EventT>, CoroutineScope by coroutineScope {

  override fun emit(event: EventT) {
    launch { events.emit(event) }
  }
}
