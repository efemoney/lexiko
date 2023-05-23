package dev.efemoney.lexiko.engine.handle

import dev.efemoney.lexiko.engine.GameContext
import dev.efemoney.lexiko.engine.StartGameEvent
import dev.efemoney.lexiko.engine.api.Board
import dev.efemoney.lexiko.engine.api.Word
import dev.efemoney.lexiko.engine.api.WordDirection
import me.tatarka.inject.annotations.Inject

@Inject
internal class StartGameHandler : GameEventHandler<StartGameEvent> {

  context(GameContext) override suspend fun handle(event: StartGameEvent) {
    println("Before start game -------------------------------------")
    println(bag)
    println(board)
    board.place(
      Word(bag.pick("hello")),
      startAt = Board.Center,
      inDirection = WordDirection.Horizontal
    )
    println("After start game --------------------------------------")
    println(bag)
    println(board)
  }
}
