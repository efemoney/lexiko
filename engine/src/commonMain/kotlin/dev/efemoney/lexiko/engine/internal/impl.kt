package dev.efemoney.lexiko.engine.internal

import dev.efemoney.lexiko.engine.Engine
import dev.efemoney.lexiko.engine.GameId
import dev.efemoney.lexiko.engine.PlayerId
import dev.efemoney.lexiko.engine.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

class AbstractEngineImpl(
  private val scope: CoroutineScope,
  override val storage: Storage,
) : Engine {

  override suspend fun createNewGame(hostId: PlayerId): GameId = coroutineScope {
    val game = storage.games.createGame(hostId)
    game.id
  }

  override suspend fun startGame(gameId: GameId) {
    TODO("Not yet implemented")
  }
}
