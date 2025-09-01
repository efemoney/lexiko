package dev.efemoney.lexiko.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain
import dev.efemoney.lexiko.app.di.AppGraph
import dev.efemoney.lexiko.lobby.LobbyScreen
import dev.efemoney.lexiko.presentation.nav3.NavDisplay
import dev.efemoney.lexiko.presentation.nav3.di.NavigationAccessors
import dev.efemoney.lexiko.presentation.nav3.rememberNavBackStack
import dev.efemoney.lexiko.ui.LexikoTheme
import dev.zacsweers.metro.asContribution

@Composable
fun LexikoEntryPoint(app: AppGraph) {
  val graph = retain(app) { app.foreground.create() }
  val navRegistry = graph.asContribution<NavigationAccessors>().registry
  val navBackStack = rememberNavBackStack(LobbyScreen)
  LexikoTheme {
    NavDisplay(
      registry = navRegistry,
      backStack = navBackStack,
    )
  }
}
