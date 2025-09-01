package dev.efemoney.lexiko.presentation.circuit.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import app.cash.molecule.RecompositionMode.ContextClock
import app.cash.molecule.launchMolecule
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitContext
import dev.efemoney.lexiko.presentation.Navigator
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.UiState
import dev.efemoney.lexiko.presentation.circuit.di.PresenterImplementation
import dev.efemoney.lexiko.presentation.circuit.di.UiImplementation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
import com.slack.circuit.runtime.Navigator as CircuitNavigator
import com.slack.circuit.runtime.presenter.Presenter as CircuitPresenter
import com.slack.circuit.runtime.screen.Screen as CircuitScreen
import com.slack.circuit.runtime.ui.Ui as CircuitUi

internal expect val PresentationContext: CoroutineContext

internal class InjectingPresenterFactory(
  private val backStack: OurBackStack,
  private val overlays: OverlayHost,
  private val implementation: (Navigator, OverlayHost) -> PresenterImplementation,
) : CircuitPresenter.Factory {
  override fun create(screen: CircuitScreen, navigator: CircuitNavigator, context: CircuitContext) = when (screen) {
    !is Screen -> null
    else -> DelegatingPresenter<Screen, UiState>(screen, implementation(OurNavigator(backStack, navigator), overlays))
  }
}

internal class InjectingUiFactory(
  private val implementation: () -> UiImplementation,
) : CircuitUi.Factory {
  override fun create(screen: CircuitScreen, context: CircuitContext) = when (screen) {
    !is Screen -> null
    else -> DelegatingUi<Screen, UiState>(screen, implementation())
  }
}

internal class DelegatingPresenter<Args : Screen, S : UiState>(
  private val screen: Args,
  private val implementation: PresenterImplementation,
) : CircuitPresenter<S> {
  @Composable
  override fun present(): S {
    return rememberRetained(screen) {
      val scope = CoroutineScope(SupervisorJob() + PresentationContext)
      val presenter = implementation.of<Args, S>(screen)
      val snapshotState = scope.produceState { presenter.run(screen) }
      object : RememberObserver {
        val value get() = snapshotState.value
        override fun onRemembered() = Unit
        override fun onAbandoned() = onForgotten()
        override fun onForgotten() = scope.cancel()
      }
    }.value
  }
}

internal class DelegatingUi<Args : Screen, S : UiState>(
  private val screen: Args,
  private val implementation: UiImplementation,
) : CircuitUi<S> {
  @Composable
  override fun Content(state: S, modifier: Modifier) {
    rememberRetained(screen) { implementation.of<Args, S>(screen) }
      .Content(state, modifier)
  }
}

internal fun <T> CoroutineScope.produceState(body: @Composable () -> T): State<T> {
  var state: MutableState<T>? = null
  launchMolecule(
    mode = ContextClock,
    emitter = {
      val outputState = state
      if (outputState != null) outputState.value = it else state = mutableStateOf(it)
    },
    body = body,
  )
  return state!!
}
