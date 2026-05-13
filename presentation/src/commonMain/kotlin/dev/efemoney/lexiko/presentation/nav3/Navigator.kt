package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import androidx.navigation3.runtime.NavBackStack
import dev.efemoney.lexiko.presentation.Navigator
import dev.efemoney.lexiko.presentation.Result
import dev.efemoney.lexiko.presentation.ReturnsResult
import dev.efemoney.lexiko.presentation.Screen

@Composable
fun rememberNavigator(backStack: NavBackStack<Screen>): Navigator {
  val results = retain(backStack) { NavResults() }
  val navigator = retain(backStack) { NavigatorImpl(backStack, results) }
  return navigator
}

internal class NavResults() {

}

private class NavigatorImpl(
  private val stack: NavBackStack<Screen>,
  private val results: NavResults,
) : Navigator {

  override fun <S : Screen> navigate(screen: S) {
    stack.add(screen)
  }

  override suspend fun <S, R : Result> navigateForResult(screen: S): R? where S : Screen, S : ReturnsResult<R> {
    TODO("Not yet implemented")
  }

  override fun pop() {
    stack.removeLast()
  }

  override fun popUntil(predicate: (Screen) -> Boolean) {
    var last: Screen
    do {
      last = stack.removeLast()
    } while (!predicate(last))
  }

  override fun <R : Result> pop(result: R) {
    TODO("Not yet implemented")
  }
}
