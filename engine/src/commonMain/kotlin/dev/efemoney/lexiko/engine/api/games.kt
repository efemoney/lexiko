package dev.efemoney.lexiko.engine.api

interface Games {

  suspend fun createGame(hostId: PlayerId): Game
}

interface Game {
  val id: GameId
  val host: Player
  val players: List<Player>
}

inline class GameId(val uuid: String)
