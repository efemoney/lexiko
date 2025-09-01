package dev.efemoney.lexiko.presentation.circuit.di

import com.slack.circuit.overlay.OverlayHost
import dev.efemoney.lexiko.presentation.Navigator
import dev.efemoney.lexiko.presentation.Presenter
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.Ui
import dev.efemoney.lexiko.presentation.UiState

interface ScreenComponent {
  fun presenter(navigator: Navigator, overlays: OverlayHost): PresenterImplementation
  fun ui(): UiImplementation
}

interface PresenterImplementation {
  fun <Args : Screen, State : UiState> of(screen: Args): Presenter<Args, State>
}

interface UiImplementation {
  fun <Args : Screen, State : UiState> of(screen: Args): Ui<State>
}
