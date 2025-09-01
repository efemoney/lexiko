package dev.efemoney.lexiko.presentation.circuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.overlay.rememberOverlayHost
import com.slack.circuit.runtime.Navigator
import dev.efemoney.lexiko.presentation.circuit.internal.InjectingPresenterFactory
import dev.efemoney.lexiko.presentation.circuit.internal.InjectingUiFactory
import dev.efemoney.lexiko.presentation.circuit.internal.OurBackStack
import dev.efemoney.lexiko.presentation.circuit.internal.rememberOurBackStack
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.circuit.di.ScreenComponent

@Composable
internal expect fun rememberNavigator(backStack: OurBackStack): Navigator

@Composable
fun ScreenHost(
  root: Screen,
  component: ScreenComponent,
  modifier: Modifier = Modifier,
) {
  val overlays = rememberOverlayHost()
  val backStack = rememberOurBackStack(root)
  val navigator = rememberNavigator(backStack)
  val circuit = remember(backStack, overlays, component) {
    Circuit.Builder()
      .addPresenterFactory(InjectingPresenterFactory(backStack, overlays, component::presenter))
      .addUiFactory(InjectingUiFactory(component::ui))
      .build()
  }
  ContentWithOverlays(modifier, overlays) {
    NavigableCircuitContent(
      navigator = navigator,
      backStack = backStack,
      modifier = Modifier,
      circuit = circuit,
    )
  }
}
