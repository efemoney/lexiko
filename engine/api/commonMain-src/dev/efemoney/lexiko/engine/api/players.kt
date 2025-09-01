package dev.efemoney.lexiko.engine.api

import kotlin.jvm.JvmInline

@JvmInline
value class PlayerId(val id: String)

@JvmInline
value class PlayerName(val value: String)

interface Player {
  val id: PlayerId
  val name: PlayerName
}

interface PlayerList {
  val players: List<Player>
}
