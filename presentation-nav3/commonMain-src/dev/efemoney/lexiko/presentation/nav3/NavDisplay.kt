package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import dev.efemoney.lexiko.presentation.Screen

@Composable
fun NavDisplay(
  registry: NavRegistry,
  backStack: NavBackStack<Screen>,
  modifier: Modifier = Modifier,
) {
  NavDisplay(
    modifier = modifier,
    backStack = backStack.current,
    onBack = backStack::onBack,
    entryDecorators = registry.entryDecorators,
    entryProvider = registry.entryProviders,
  )
}
