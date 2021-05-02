package dev.efemoney.lexiko.local

import dev.efemoney.lexiko.TestPlayers
import dev.efemoney.lexiko.engine.api.PlayerId
import dev.efemoney.lexiko.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalGamesTest {

  @Test
  fun testGivenExistingPlayerCreateGameReturnsANewGame() = runTest {
    val playerId = PlayerId("android")
    val games = LocalGames(players = TestPlayers(playerId))
    val game = games.createGame(playerId)

    assertEquals(playerId, game.host.id)
    assertTrue(game.players.isEmpty())
  }
}
