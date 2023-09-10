package dev.efemoney.lexiko.engine.events

import dev.efemoney.lexiko.engine.GameContext
import dev.efemoney.lexiko.engine.api.Board
import dev.efemoney.lexiko.engine.api.Direction
import me.tatarka.inject.annotations.Inject

data object StartGameEvent : GameEvent {

  @Inject internal class Handler : GameEventHandler<StartGameEvent> {
    context(GameContext) override suspend fun handle(event: StartGameEvent) {
      board.place(
        tiles = bag.pick("hello"),
        startAt = Board.Center,
        direction = Direction.Horizontal,
      )
    }
  }
}
