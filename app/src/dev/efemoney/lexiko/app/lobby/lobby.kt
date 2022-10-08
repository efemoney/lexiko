package dev.efemoney.lexiko.app.lobby

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.efemoney.lexiko.app.R
import dev.efemoney.lexiko.app.internal.component
import dev.efemoney.lexiko.app.ui.LexikoTheme
import dev.efemoney.lexiko.app.ui.coolBlue
import dev.efemoney.lexiko.app.ui.grayish
import dev.efemoney.lexiko.lobby.Avatar
import dev.efemoney.lexiko.lobby.Friend
import dev.efemoney.lexiko.lobby.Game
import dev.efemoney.lexiko.lobby.GameOrFriend

private val AppBarMinHeight = 72.dp
private val RowHeight = 64.dp
private val AvatarSize = 48.dp
private val AvatarOffset = 14.dp

private fun Modifier.rowSize() = fillMaxWidth().heightIn(min = RowHeight)

@Composable
fun LobbyScreen() {

  val lobbyUiState by component()
    .lobbyPresenter
    .uiState
    .collectAsState()

  LobbyUi(
    lobbyUiState.gamesAndFriends,
    lobbyUiState.onClickProfile
  )
}

@Composable
fun LobbyUi(
  gamesAndFriends: List<GameOrFriend> = emptyList(),
  onClickProfile: () -> Unit = {},
) {
  Scaffold(
    backgroundColor = MaterialTheme.colors.background,
    topBar = { AppBar(onClickProfile) },
    content = { GamesAndFriendsList(gamesAndFriends, Modifier.padding(it)) }
  )
}

@Composable
private fun AppBar(onClickProfile: () -> Unit = {}) {
  TopAppBar(
    backgroundColor = MaterialTheme.colors.surface,
    modifier = Modifier.heightIn(min = AppBarMinHeight),
    title = { Text("Lobby") },
    actions = { ProfileAction(onClickProfile) },
  )
}

@Composable
private fun ProfileAction(onClickProfile: () -> Unit) {
  IconButton(onClick = onClickProfile) {
    Icon(
      imageVector = Icons.Default.AccountCircle,
      contentDescription = null,
      tint = coolBlue,
    )
  }
}

@Composable
private fun GamesAndFriendsList(
  gamesAndFriends: List<GameOrFriend>,
  modifier: Modifier = Modifier,
) {
  LazyColumn(modifier = modifier.fillMaxSize()) {
    items(gamesAndFriends) {
      when (it) {
        is Game -> OngoingGame(it)
        is Friend -> AddedFriend(it)
      }
      Spacer(Modifier.height(2.dp))
    }
  }
}

@Composable
private fun OngoingGame(game: Game) {
  Surface(Modifier.rowSize()) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Spacer(Modifier.width(16.dp))
      game.players.forEachIndexed { index, player ->
        PlayerAvatar(
          avatar = player.avatar,
          modifier = Modifier.offset(x = -AvatarOffset * index),
        )
      }
      Spacer(Modifier.weight(1f))
      if (game.spectatorCount > 0) {
        SpectatorCount(game.spectatorCount)
        Spacer(Modifier.width(16.dp))
      }
    }
  }
}

@Composable
private fun SpectatorCount(spectatorCount: Int) {

  if (spectatorCount <= 0) return

  CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
    Row(
      Modifier
        .border(
          width = 1.dp,
          shape = RoundedCornerShape(10.dp),
          color = grayish,
        )
        .padding(horizontal = 8.dp)
        .wrapContentWidth()
        .height(24.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        modifier = Modifier.size(16.dp),
        imageVector = Icons.Default.Visibility,
        contentDescription = null,
      )

      Spacer(Modifier.width(4.dp))

      Text(
        modifier = Modifier,
        text = spectatorCount.toString(),
        fontSize = 12.sp,
      )
    }
  }
}

@Composable
private fun PlayerAvatar(avatar: Avatar, modifier: Modifier = Modifier) {
  AsyncImage(
    model = avatar,
    placeholder = painterResource(R.drawable.avatar_placeholder),
    contentDescription = null,
    modifier = modifier
      .border(2.dp, color = MaterialTheme.colors.surface, CircleShape)
      .clip(CircleShape)
      .size(AvatarSize),
  )
}

@Composable
private fun AddedFriend(friend: Friend) {
  Surface(Modifier.rowSize()) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Spacer(Modifier.width(16.dp))
      PlayerAvatar(friend.avatar)
      Spacer(Modifier.width(8.dp))
      Text(text = friend.name.value)
      Spacer(Modifier.weight(1f))
      Spacer(Modifier.width(16.dp))
    }
  }
}

@Preview(name = "Lobby Screen")
@Composable
fun LobbyPreview(@PreviewParameter(GamesAndFriends::class) gamesAndFriends: List<GameOrFriend>) {
  LexikoTheme {
    LobbyUi(gamesAndFriends)
  }
}
