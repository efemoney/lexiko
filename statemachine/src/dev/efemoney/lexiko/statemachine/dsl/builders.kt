package dev.efemoney.lexiko.statemachine.dsl

import StateMachineBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@StateMachineDsl
inline fun <
  reified StateT : Any,
  EventT : Any
  > CoroutineScope.StateMachine(
  initialState: Any? = null,
  coroutineContext: CoroutineContext = EmptyCoroutineContext,
  @BuilderInference builder: StateMachineBuilder<StateT, EventT>.() -> Unit
) = StateMachineBuilder<StateT, EventT>(
  coroutineScope = this,/*CoroutineScope(newCoroutineContext(coroutineContext))*/
  baseType = StateT::class,
  initialState = initialState as StateT?,
).apply(builder).build()

@DelicateCoroutinesApi
@StateMachineDsl
inline fun <
  reified StateT : Any,
  reified EventT : Any
  > StateMachine(
  initialState: Any? = null,
  coroutineContext: CoroutineContext = EmptyCoroutineContext,
  @BuilderInference builder: StateMachineBuilder<StateT, EventT>.() -> Unit
) = GlobalScope.StateMachine(initialState, coroutineContext, builder)
