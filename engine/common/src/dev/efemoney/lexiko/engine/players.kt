package dev.efemoney.lexiko.engine

import kotlin.jvm.JvmInline

interface Player {
  val id: PlayerId
  val name: PlayerName
}

@JvmInline
value class PlayerId(val uid: String)

@JvmInline
value class PlayerName(val name: String)
