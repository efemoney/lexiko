package dev.efemoney.lexiko.engine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.efemoney.lexiko.engine.api.Board
import dev.efemoney.lexiko.engine.api.PlayerName
import dev.efemoney.lexiko.engine.handle.GameEventHandlers
import dev.efemoney.lexiko.engine.impl.BagOfTilesImpl
import dev.efemoney.lexiko.engine.impl.BoardImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

internal class GameContext(
  private val scope: CoroutineScope,
  private val events: Channel<GameEvent>,
  val bag: BagOfTilesImpl,
  val board: BoardImpl,
) {
  fun send(event: GameEvent) {
    scope.launch { events.send(event) }
  }
}

@Inject
class Game internal constructor(
  private val handlers: GameEventHandlers,
) {

  @Composable fun present(events: Channel<GameEvent>): GameState {
    val scope = rememberCoroutineScope()
    val context = remember(scope, events) {
      GameContext(scope, events, BagOfTilesImpl(), BoardImpl())
    }
    with(context) {
      LaunchedEffect(context, events) {
        for (event in events) handlers.handle(event)
      }
      return GameState(players(), board())
    }
  }

  context(GameContext)
  @Composable private fun board(): Board {
    return board
  }

  context(GameContext)
  @Composable private fun players(): GameState.Players {
    return remember { GameState.Players(emptyList()) }
  }
}

data class GameState(
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

sealed interface GameEvent

data object StartGameEvent : GameEvent


