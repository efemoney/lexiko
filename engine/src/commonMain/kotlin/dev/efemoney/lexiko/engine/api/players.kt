package dev.efemoney.lexiko.engine.api

interface Players {

  suspend fun findById(playerId: PlayerId): Player?

  suspend fun createPlayer(playerId: PlayerId): Player
}

interface Player {
  val id: PlayerId
}

inline class PlayerId(val platformUid: String)
