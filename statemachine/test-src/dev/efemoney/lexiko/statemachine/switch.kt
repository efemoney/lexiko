@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")

package dev.efemoney.lexiko.statemachine

import app.cash.turbine.test
import dev.efemoney.lexiko.statemachine.SwitchState.Off
import dev.efemoney.lexiko.statemachine.SwitchState.On
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SwitchTest {

  @Test
  fun test() = runBlockingTest {

    val stateMachine = StateMachine(initialState = On) {

      state<On> {
        on<Toggle> {
          transition(Off)
        }
      }

      state<Off> {
        on<Toggle> { transition(On) }
      }
    }

    stateMachine.state.test {
      assertEquals(expected = On, actual = awaitItem())

      stateMachine.process(Toggle)
      assertEquals(expected = Off, actual = awaitItem())

      stateMachine.process(Toggle)
      assertEquals(expected = On, actual = awaitItem())

      cancelAndIgnoreRemainingEvents()
    }
  }
}

sealed interface SwitchState {
  object On : SwitchState
  object Off : SwitchState
}

object Toggle
