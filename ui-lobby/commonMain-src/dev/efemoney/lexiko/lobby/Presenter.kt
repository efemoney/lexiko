package dev.efemoney.lexiko.lobby

import androidx.compose.runtime.Composable
import dev.drewhamilton.poko.Poko
import dev.efemoney.lexiko.engine.api.Game
import dev.efemoney.lexiko.presentation.Presenter
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.UiState
import dev.zacsweers.metro.Inject

object LobbyScreen : Screen

@Inject
internal class LobbyPresenter(
  private val service: LobbyService,
) : Presenter<LobbyScreen, LobbyUiState> {
  @Composable
  override fun run(screen: LobbyScreen): LobbyUiState {
    TODO()
  }
}

@Poko
internal class LobbyUiState(val game: Game) : UiState
