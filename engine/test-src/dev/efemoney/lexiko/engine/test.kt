package dev.efemoney.lexiko.engine

import app.cash.molecule.RecompositionClock.Immediate
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import dev.efemoney.lexiko.engine.handle.InjectHandlersComponent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.time.Duration

class GameTest {

  @Test
  fun run() = runTest(timeout = Duration.INFINITE) {
    val game = InjectEngineComponent(handlers = InjectHandlersComponent()).createGame()
    val events = Channel<GameEvent>()

    moleculeFlow(Immediate) { game.present(events) }.test(timeout = Duration.INFINITE) {
      backgroundScope.launch { events.send(StartGameEvent) }
      while (true) awaitEvent().run { if (isTerminal) break }
    }
  }
}
