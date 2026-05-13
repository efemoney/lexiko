package dev.efemoney.lexiko.lobby

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import dev.drewhamilton.poko.Poko
import dev.efemoney.lexiko.lobby.internal.LobbyService
import dev.efemoney.lexiko.presentation.Presenter
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.UiState
import dev.efemoney.lexiko.ui.AppBarState
import dev.zacsweers.metro.Inject

object LobbyScreen : Screen

@Poko
internal class LobbyUiState(
  val appBar: AppBarState = AppBarState("Lobby"),
  val toast: SnackbarHostState = SnackbarHostState(),
) : UiState

@Inject
internal class LobbyPresenter(
  private val service: LobbyService,
) : Presenter<LobbyScreen, LobbyUiState> {
  @Composable
  override fun run(screen: LobbyScreen): LobbyUiState {
    return LobbyUiState()
  }
}
