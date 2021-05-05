package dev.efemoney.lexiko.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

class Coordinator(
  private val hostId: PlayerId,
  private val games: Games,
  private val players: Players,
  private val store: Store,
) {
  private val scope: CoroutineScope = TODO()

  suspend fun createNewGame(hostId: PlayerId, andInvite: List<PlayerId>): GameId = coroutineScope {


    TODO()
  }
}
