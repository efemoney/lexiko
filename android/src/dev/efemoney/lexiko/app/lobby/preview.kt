@file:Suppress("SpellCheckingInspection")

package dev.efemoney.lexiko.app.lobby

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.efemoney.lexiko.lobby.Avatar
import dev.efemoney.lexiko.lobby.Friend
import dev.efemoney.lexiko.lobby.Game
import dev.efemoney.lexiko.lobby.GameOrFriend
import dev.efemoney.lexiko.lobby.Host
import dev.efemoney.lexiko.lobby.Name

private val host = Host(Name("Efeturi Money"), Avatar(""))

private val friends = listOf(
  Friend(Name("Oluwasegun Famisa"), Avatar(""), onClick = {}),
  Friend(Name("Moyinoluwa Adeyemi"), Avatar(""), onClick = {}),
  Friend(Name("Mayowa Adegeye"), Avatar(""), onClick = {}),
)

class GamesAndFriends : PreviewParameterProvider<List<GameOrFriend>> {

  override val values = sequenceOf(
    listOf(
      Game(
        players = listOf(host, friends[0]),
        spectatorCount = 0,
        onClick = {}
      ),
      Game(
        players = listOf(host, friends[1]),
        spectatorCount = 9,
        onClick = {}
      ),
      friends[0],
      friends[1],
      friends[2],
    )
  )

  override val count = 1
}
