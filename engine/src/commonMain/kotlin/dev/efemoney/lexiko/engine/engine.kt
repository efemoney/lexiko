package dev.efemoney.lexiko.engine

interface Engine {

  val storage: Storage

  suspend fun createNewGame(hostId: PlayerId): GameId

  suspend fun startGame(gameId: GameId)
}
