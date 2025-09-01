package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import dev.efemoney.lexiko.presentation.Screen

interface NavBackStack<Key : Screen> {
  val current: List<Screen>
  fun onBack(popCount: Int)
}

@Composable
fun rememberNavBackStack(initialScreen: Screen? = null): NavBackStack<Screen> {
  return remember {
    NavBackStackImpl(initialScreen)
  }
}

internal class NavBackStackImpl(
  initialScreen: Screen?,
) : NavBackStack<Screen> {
  private val stack = mutableStateListOf<Screen>()

  override val current get() = stack.toList()
  override fun onBack(popCount: Int) {

  }
}
