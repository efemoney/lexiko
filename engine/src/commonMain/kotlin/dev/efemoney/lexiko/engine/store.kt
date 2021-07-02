package dev.efemoney.lexiko.engine

interface Storage {
  val games: GamesStorage
  val players: PlayersStorage
}

interface GamesStorage {
  suspend fun createGame(hostId: PlayerId): Game
}

interface PlayersStorage {
  suspend fun findById(playerId: PlayerId): Player?
  suspend fun createPlayer(playerId: PlayerId): Player
}
