@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")

package dev.efemoney.lexiko.statemachine

import app.cash.turbine.test
import dev.efemoney.lexiko.statemachine.TrafficLight.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class StateMachineTest {

  @Test
  fun test() = runBlockingTest {

    val stateMachine = StateMachineOf<TrafficLight, Unit> {

      initialState(Red)

      state<Red> {
        onEnter {
          delay(5.seconds)
          transition(Yellow)
        }
      }

      state<Yellow> {
        onEnter {
          delay(10.seconds)
          transition(Green)
        }
      }

      state<Green> {
        onEnter {
          delay(15.seconds)
          transition(Red)
        }
      }
    }

    stateMachine.state.test {

    }
  }
}

sealed interface TrafficLight {
  object Red : TrafficLight
  object Yellow : TrafficLight
  object Green : TrafficLight
}
