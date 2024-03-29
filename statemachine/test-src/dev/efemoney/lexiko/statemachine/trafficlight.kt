package dev.efemoney.lexiko.statemachine

import app.cash.turbine.test
import dev.efemoney.lexiko.statemachine.TrafficLight.Green
import dev.efemoney.lexiko.statemachine.TrafficLight.Red
import dev.efemoney.lexiko.statemachine.TrafficLight.Yellow
import dev.efemoney.lexiko.statemachine.dsl.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TrafficLightTest {

  @Test
  fun run() = runTest(UnconfinedTestDispatcher()) {

    val trafficConfig = TrafficLightConfiguration(
      red = 5.seconds,
      yellow = 10.seconds,
      green = 15.seconds,
    )

    with(trafficLightsStateMachine(trafficConfig)) {
      state.test {
        advanceTimeBy(1.seconds) // throwaway duration to offset runTest delay behavior

        repeat(5) {
          assertEquals(Red, awaitItem())
          advanceTimeBy(trafficConfig.red)

          assertEquals(Yellow, awaitItem())
          advanceTimeBy(trafficConfig.yellow)

          assertEquals(Green, awaitItem())
          advanceTimeBy(trafficConfig.green)
        }

        cancelAndIgnoreRemainingEvents()
      }

      cancel()
    }
  }

  private fun CoroutineScope.trafficLightsStateMachine(config: TrafficLightConfiguration) = StateMachine {

    initialState(Red)

    onEnterAny {
      delay(config.durationFor(state))
      emit(Next)
    }

    state<Red> {
      on<Next> { transition(Yellow) }
    }

    state<Yellow> {
      on<Next> { transition(Green) }
    }

    state<Green> {
      on<Next> { transition(Red) }
    }
  }
}

class TrafficLightConfiguration(val red: Duration, val yellow: Duration, val green: Duration) {
  fun durationFor(state: TrafficLight) = when (state) {
    Red -> red
    Yellow -> yellow
    Green -> green
  }
}

sealed interface TrafficLight {
  data object Red : TrafficLight
  data object Yellow : TrafficLight
  data object Green : TrafficLight
}

typealias Next = Unit
