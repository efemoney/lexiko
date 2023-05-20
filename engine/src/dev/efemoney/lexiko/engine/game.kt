package dev.efemoney.lexiko.engine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.efemoney.lexiko.engine.api.Board
import dev.efemoney.lexiko.engine.api.PlayerName
import dev.efemoney.lexiko.engine.impl.BoardImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class Lexiko {

  @Composable fun present(events: MutableSharedFlow<GameEvent>): Game {
    val scope = rememberCoroutineScope()
    val context = remember(scope, events) { GameContext(scope, events, BoardImpl()) }

    with(context) {
      return Game(
        board = board,
        players = players(),
      )
    }
  }

  context(GameContext)
  @Composable fun board(): Board {
    return board
  }

  context(GameContext)
  @Composable fun players(): Game.Players {
    TODO()
  }
}

internal class GameContext(
  private val scope: CoroutineScope,
  private val events: MutableSharedFlow<GameEvent>,
  val board: BoardImpl,
) {
  fun send(event: GameEvent) {
    scope.launch { events.emit(event) }
  }
}

data class Game(
  val players: Players,
  val board: Board,
) {

  data class Players(
    val list: List<Player>,
  ) : List<Player> by list

  data class Player(
    val name: PlayerName,
  )
}

sealed interface GameEvent {
  data object On : GameEvent
}
