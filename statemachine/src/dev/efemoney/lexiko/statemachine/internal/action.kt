package dev.efemoney.lexiko.statemachine.internal

import dev.efemoney.lexiko.statemachine.dsl.ActionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

internal class ActionScopeImpl<SpecificStateT : StateT, StateT : Any, EventT : Any>(
  override val state: SpecificStateT,
  private val events: MutableSharedFlow<Event<EventT>>,
  private val coroutineScope: CoroutineScope,
) : ActionScope<SpecificStateT, StateT, EventT> {

  override fun emit(event: EventT) {
    coroutineScope.launch { events.emit(Event.RunAction(event)) }
  }
}
