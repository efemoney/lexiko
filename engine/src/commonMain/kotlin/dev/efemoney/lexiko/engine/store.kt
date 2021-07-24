package dev.efemoney.lexiko.engine

interface Store : GamesStore, PlayersStore

interface GamesStore {
  suspend fun createGame(hostId: PlayerId): Game
}

interface PlayersStore {
  suspend fun findById(playerId: PlayerId): Player?
  suspend fun createPlayer(playerId: PlayerId): Player
}
