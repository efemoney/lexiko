package dev.efemoney.lexiko.statemachine

@DslMarker
internal annotation class StateMachineDsl

//@StateMachineDsl
//inline fun <reified T, SpecificStateT, StateT, ActionT> StateBuilder<SpecificStateT, StateT, ActionT>.on(
//  enum: T,
//  noinline transition: StateTransition<SpecificStateT, T, StateT, ActionT>,
//) where
//  StateT : Any,
//  SpecificStateT : StateT,
//  ActionT : Enum<ActionT>,
//  T : ActionT = on(enum::class, { action == enum }, transition)
