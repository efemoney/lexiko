package dev.efemoney.lexiko.engine

interface Player {
  val id: PlayerId
  val name: PlayerName
}

@JvmInline
value class PlayerId(val uid: String)

@JvmInline
value class PlayerName(val name: String)
