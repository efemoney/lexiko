package dev.efemoney.lexiko.engine

import kotlin.jvm.JvmInline

interface Players {

  suspend fun findById(playerId: PlayerId): Player?

  suspend fun createPlayer(playerId: PlayerId): Player
}

interface Player {
  val id: PlayerId
}

@JvmInline
value class PlayerId(val platformUid: String)
