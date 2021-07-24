package dev.efemoney.lexiko.engine

interface Engine {
  val store: Store
  suspend fun createGame(hostId: PlayerId): Game
}
