package dev.efemoney.lexiko.engine

interface PlayerContext {
  val currentPlayer: Player
  suspend fun Game.start() = start(currentPlayer.id)
}
