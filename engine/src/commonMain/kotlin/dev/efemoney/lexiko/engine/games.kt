package dev.efemoney.lexiko.engine

import kotlin.jvm.JvmInline

interface Game {
  val id: GameId
  val state: GameState
  val hostId: PlayerId
  val players: List<Player>
}

sealed interface GameState {
  interface Initialized : GameState
  interface WaitingForPlayers : GameState
  interface Started : GameState
  interface WaitingForTurn : GameState {
    val forPlayer: PlayerId
  }
}

@JvmInline
value class GameId(val uuid: String)
