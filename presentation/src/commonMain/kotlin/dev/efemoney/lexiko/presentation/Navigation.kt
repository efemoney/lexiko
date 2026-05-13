package dev.efemoney.lexiko.presentation

import androidx.compose.runtime.Immutable

interface Navigator {

  fun <S> navigate(screen: S) where S : Screen

  suspend
  fun <S, R> navigateForResult(screen: S): R? where S : Screen, R : Result, S : ReturnsResult<R>

  fun pop()

  fun <R> pop(result: R) where R : Result

  fun popUntil(predicate: (Screen) -> Boolean)
}

@Immutable
interface Result

interface ReturnsResult<R : Result>
