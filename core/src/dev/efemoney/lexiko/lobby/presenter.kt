package dev.efemoney.lexiko.lobby

import dev.efemoney.lexiko.internal.Dispatchers
import dev.efemoney.lexiko.internal.ForegroundScope
import dev.efemoney.lexiko.internal.getValue
import dev.efemoney.lexiko.internal.setValue
import dev.efemoney.lexiko.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LobbyPresenter @Inject internal constructor(
  private val dispatchers: Dispatchers,
  private val scope: ForegroundScope,
  private val navigator: Navigator,
) {

  val uiState: StateFlow<LobbyUiState>

  private var state by MutableStateFlow(LobbyUiState(navigator::gotoProfile))
    .also { uiState = it.asStateFlow() }

  init {
    scope.loadGamesAndFriends()
  }

  private fun CoroutineScope.loadGamesAndFriends() = launch {

  }
}

data class LobbyUiState(
  val onClickProfile: () -> Unit,
  val gamesAndFriends: List<GameOrFriend> = emptyList(),
)

sealed interface GameOrFriend

data class Game(
  val players: List<Player>,
  val spectatorCount: Int,
  val onClick: () -> Unit,
) : GameOrFriend

sealed interface Player {
  val name: Name
  val avatar: Avatar
}

data class Host(
  override val name: Name,
  override val avatar: Avatar,
) : Player

data class Friend(
  override val name: Name,
  override val avatar: Avatar,
  val onClick: () -> Unit,
) : Player, GameOrFriend

@JvmInline
value class Name(val value: String)

@JvmInline
value class Avatar(val model: Any?)
