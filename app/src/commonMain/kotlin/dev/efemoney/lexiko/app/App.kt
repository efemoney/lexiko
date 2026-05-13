package dev.efemoney.lexiko.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import dev.efemoney.lexiko.app.di.NavRegistryAccessor
import dev.efemoney.lexiko.di.ForegroundScope
import dev.efemoney.lexiko.di.castAs
import dev.efemoney.lexiko.lobby.LobbyScreen
import dev.efemoney.lexiko.presentation.nav3.NavDisplay
import dev.efemoney.lexiko.ui.LexikoTheme

@Composable
inline fun LexikoEntryPoint(
  crossinline foregroundGraph: () -> ForegroundScope.Graph,
) {
  val graph = retain { foregroundGraph() }
  LexikoTheme {
    NavDisplay(
      navRoot = LobbyScreen,
      navRegistry = graph.castAs<NavRegistryAccessor>().registry,
    )
  }
}
