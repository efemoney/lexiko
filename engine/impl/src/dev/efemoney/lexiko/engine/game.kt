package dev.efemoney.lexiko.engine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.efemoney.lexiko.engine.api.BagOfTiles
import dev.efemoney.lexiko.engine.api.Board
import dev.efemoney.lexiko.engine.api.PlayerId
import dev.efemoney.lexiko.engine.events.GameEvent
import dev.efemoney.lexiko.engine.events.GameEventHandlers
import dev.efemoney.lexiko.engine.impl.BoardImpl
import dev.efemoney.lexiko.engine.impl.DefaultBagOfTiles
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

internal class GameContext(
  private val scope: CoroutineScope,
  private val events: Channel<GameEvent>,
  val host: PlayerId,
  val players: SnapshotStateList<PlayerId>,
  val board: BoardImpl,
  val bag: DefaultBagOfTiles,
) {
  fun send(event: GameEvent) = scope.launch { events.send(event) }
}

@Inject
class Game internal constructor(private val eventHandlers: GameEventHandlers) {

  @Composable
  fun rememberGameState(host: PlayerId, events: Channel<GameEvent>): GameState =
    with(rememberGameContext(host, events)) { GameState(bag, board) }

  @Composable
  private fun rememberGameContext(host: PlayerId, events: Channel<GameEvent>): GameContext {
    val scope = rememberCoroutineScope()
    val context = remember(scope, events, host) {
      GameContext(
        scope = scope,
        events = events,
        host = host,
        players = mutableStateListOf(host),
        board = BoardImpl(),
        bag = DefaultBagOfTiles(),
      )
    }
    LaunchedEffect(context) {
      // handle events concurrently in separate coroutines
      for (event in events) launch(CoroutineName("Handling: $event")) {
        with(context) { eventHandlers.handle(event) }
      }
    }
    return context
  }
}

data class GameState(val bag: BagOfTiles, val board: Board)
