package dev.efemoney.lexiko.engine.internal

import dev.efemoney.lexiko.engine.Engine
import dev.efemoney.lexiko.engine.Game
import dev.efemoney.lexiko.engine.PlayerId
import dev.efemoney.lexiko.engine.Store
import kotlinx.coroutines.CoroutineScope

class AbstractEngineImpl(
  private val scope: CoroutineScope,
  override val store: Store,
) : Engine {

  override suspend fun createGame(hostId: PlayerId): Game = store.createGame(hostId)
}
