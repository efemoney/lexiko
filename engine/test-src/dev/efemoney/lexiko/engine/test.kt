package dev.efemoney.lexiko.engine

import app.cash.molecule.RecompositionMode.Immediate
import app.cash.molecule.moleculeFlow
import app.cash.turbine.Event
import app.cash.turbine.test
import dev.efemoney.lexiko.engine.api.PlayerId
import dev.efemoney.lexiko.engine.di.InjectEngineComponent
import dev.efemoney.lexiko.engine.di.InjectEventHandlersComponent
import dev.efemoney.lexiko.engine.events.GameEvent
import dev.efemoney.lexiko.engine.events.StartGameEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.time.Duration

class GameTest {

  @Test
  fun run() = runTest(timeout = Duration.INFINITE) {
    val events = Channel<GameEvent>()
    val game = InjectEngineComponent(InjectEventHandlersComponent()).newGame()

    moleculeFlow(Immediate) {
      game.rememberGameState(
        host = PlayerId("one"),
        events = events,
      )
    }.test(timeout = Duration.INFINITE) {
      backgroundScope.launch {
        events.send(StartGameEvent)
      }
      while (true) awaitEvent().let {
        if (it is Event.Item) println(it.value)
        if (it.isTerminal) break
      }
    }
  }
}
