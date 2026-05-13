package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.efemoney.lexiko.presentation.Screen
import kotlinx.serialization.serializer

@Composable
fun NavDisplay(
  navRoot: Screen,
  navRegistry: NavRegistry,
  modifier: Modifier = Modifier,
) {
  val backStack = rememberSerializable(serializer = serializer()) { NavBackStack(navRoot) }
  NavDisplay(
    modifier = modifier,
    backStack = backStack,
    entryDecorators = navRegistry.entryDecorators,
    entryProvider = navRegistry.entryProviders,
  )
}
