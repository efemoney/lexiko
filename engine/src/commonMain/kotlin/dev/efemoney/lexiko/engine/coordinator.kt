package dev.efemoney.lexiko.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

class Coordinator(
  private val games: Games,
  private val players: Players,
  private val coordinatorScope: CoroutineScope = TODO(),
) {

  suspend fun createNewGame(hostId: PlayerId, invitees: List<PlayerId> = emptyList()): GameId = coroutineScope {
    TODO()
  }
}
