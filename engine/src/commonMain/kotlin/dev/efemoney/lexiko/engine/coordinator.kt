package dev.efemoney.lexiko.engine

import dev.efemoney.lexiko.engine.api.Games
import dev.efemoney.lexiko.engine.api.PlayerId
import dev.efemoney.lexiko.engine.api.Players
import kotlinx.coroutines.CoroutineScope

class Coordinator(
  private val hostId: PlayerId,
  private val games: Games,
  private val players: Players,
) {
  private val scope: CoroutineScope = TODO()

}
