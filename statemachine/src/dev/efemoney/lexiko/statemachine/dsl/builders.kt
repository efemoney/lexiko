package dev.efemoney.lexiko.statemachine.dsl

import StateMachineBuilder
import dev.efemoney.lexiko.statemachine.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlin.reflect.KClass

@PublishedApi
internal fun <StateT : Any, EventT : Any> CoroutineScope.StateMachine(
  stateType: KClass<StateT>,
  initialState: StateT? = null,
  builder: StateMachineBuilder<StateT, EventT>.() -> Unit
) = StateMachineBuilder<_, EventT>(stateType, initialState)
  .apply(builder)
  .build()
  .also { it.runIn(this) }

@StateMachineDsl
inline fun <reified StateT : Any, EventT : Any> CoroutineScope.StateMachine(
  initialState: Any? = null,
  noinline builder: StateMachineBuilder<StateT, EventT>.() -> Unit
): StateMachine<StateT, EventT> = StateMachine(StateT::class, initialState as StateT?, builder)

@DelicateCoroutinesApi
@StateMachineDsl
inline fun <reified StateT : Any, reified EventT : Any> StateMachine(
  initialState: Any? = null,
  noinline builder: StateMachineBuilder<StateT, EventT>.() -> Unit
): StateMachine<StateT, EventT> = GlobalScope.StateMachine(initialState, builder)
