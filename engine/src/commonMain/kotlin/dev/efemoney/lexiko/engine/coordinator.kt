package dev.efemoney.lexiko.engine

import kotlinx.coroutines.CoroutineScope

class Coordinator(
  private val host: Player,
  private val games: Games,
  private val players: Players,
  private val coroutines: CoroutineScope,
) {
  var currentGame: Game? = null


}
