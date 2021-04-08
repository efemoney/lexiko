package dev.efemoney.lexiko.engine

interface Players {
  suspend fun createPlayer(platformUid: String): Player
}

interface Player {
  val id: PlayerId
}

inline class PlayerId(val platformUid: String)
