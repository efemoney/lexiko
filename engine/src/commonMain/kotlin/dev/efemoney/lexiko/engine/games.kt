package dev.efemoney.lexiko.engine

interface Games {

  suspend fun createGame(hostId: PlayerId): Game
}

interface Game {
  val id: GameId
  val state: GameState
  val hostId: PlayerId
  val players: List<Player>
}

/*sealed*/ interface GameState

inline class GameId(val uuid: String)
