package dev.efemoney.lexiko.engine

import kotlin.jvm.JvmInline

interface Game {
  val hostId: PlayerId
  val id: GameId
  val code: GameCode
  val state: GameState

  suspend fun start(starterId: PlayerId)

  suspend fun addPlayer(id: PlayerId)
}

interface GameAction {
  suspend fun execute()
}

interface GameLoop {
  suspend fun GameState.process(action: GameAction): GameState
}

interface GameFactory {
  fun create(hostId: GameId): Game
}

sealed interface GameState
interface WaitingForPlayers : GameState
sealed interface InProgress : GameState
interface WaitingForTurn : InProgress {
  val playerId: PlayerId
}

@JvmInline
value class GameId(val uid: String)

@JvmInline
value class GameCode(val code: String)
