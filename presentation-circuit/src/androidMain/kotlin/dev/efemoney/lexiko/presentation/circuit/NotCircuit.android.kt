package dev.efemoney.lexiko.presentation.circuit

import androidx.compose.runtime.Composable
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import dev.efemoney.lexiko.presentation.circuit.internal.OurBackStack

@Composable
internal actual fun rememberNavigator(backStack: OurBackStack): Navigator {
  return rememberCircuitNavigator(backStack = backStack, enableBackHandler = true)
}
