package dev.efemoney.lexiko.lobby

import dev.efemoney.lexiko.internal.Dispatchers
import dev.efemoney.lexiko.internal.Inject
import dev.efemoney.lexiko.internal.RetainedScope
import dev.efemoney.lexiko.internal.getValue
import dev.efemoney.lexiko.internal.setValue
import dev.efemoney.lexiko.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LobbyPresenter @Inject internal constructor(
  private val dispatchers: Dispatchers,
  private val scope: RetainedScope,
  private val navigator: Navigator,
) {

  val uiState: StateFlow<LobbyUiState>

  private var state by MutableStateFlow(LobbyUiState(::onProfileCLicked))
    .also { uiState = it.asStateFlow() }

  init {
    scope.launchLoadGamesAndFriendsList()
  }

  private fun CoroutineScope.launchLoadGamesAndFriendsList() = launch {

  }

  private fun onProfileCLicked() = navigator.gotoProfile()
}

data class LobbyUiState(
  val onClickProfile: () -> Unit,
  val gamesAndFriends: List<GameOrFriend> = emptyList(),
)

interface GameOrFriend

data class Game(
  val players: List<Player>,
  val spectatorCount: Int,
  val onClick: () -> Unit,
) : GameOrFriend

sealed class Player {
  abstract val name: Name
  abstract val avatar: Avatar
}

data class Host(
  override val name: Name,
  override val avatar: Avatar
) : Player()

data class Friend(
  override val name: Name,
  override val avatar: Avatar,
  val onClick: () -> Unit,
) : Player(), GameOrFriend

inline class Name(val value: String)

inline class Avatar(val model: Any?)
