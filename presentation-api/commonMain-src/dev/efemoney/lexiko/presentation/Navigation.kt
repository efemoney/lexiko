package dev.efemoney.lexiko.presentation

import androidx.compose.runtime.Immutable

interface Navigator {
  fun <S> navigateTo(screen: S) where S : Screen
  fun <S> pop(screen: S) where S : Screen

  suspend
  fun <S, R> navigateForResult(screen: S): R? where S : Screen, R : Result, S : ReturnsResult<R>
  fun <S, R> pop(screen: S, result: R) where S : Screen, R : Result, S : ReturnsResult<R>

  fun popUntil(predicate: (Screen) -> Boolean)
}

inline fun <reified S : Screen> Navigator.popUntil(crossinline predicate: S.() -> Boolean) =
  popUntil { it is S && it.predicate() }

@Immutable
interface Result

interface ReturnsResult<R : Result>
