@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")

package dev.efemoney.lexiko.statemachine

import app.cash.turbine.test
import dev.efemoney.lexiko.statemachine.TrafficLight.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class TrafficLightTest {

  @Test
  fun test() = runTest {
    trafficLightsStateMachine().state.test {

    }
  }

  private fun CoroutineScope.trafficLightsStateMachine() = StateMachine {

    initialState(Red)

    state<Red> {
      onEnter { delay(5.seconds) }
      on<Next> { transition(Yellow) }
    }

    state<Yellow> {
      onEnter { delay(10.seconds) }
      on<Next> { transition(Green) }
    }

    state<Green> {
      onEnter { delay(15.seconds) }
      on<Next> { transition(Red) }
    }
  }
}

sealed interface TrafficLight {
  object Red : TrafficLight
  object Yellow : TrafficLight
  object Green : TrafficLight
}

typealias Next = Unit
