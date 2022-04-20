@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")

package dev.efemoney.lexiko.statemachine

import dev.efemoney.lexiko.statemachine.SwitchState.Off
import dev.efemoney.lexiko.statemachine.SwitchState.On
import dev.efemoney.lexiko.statemachine.ToggleEnum.SwitchOff
import dev.efemoney.lexiko.statemachine.ToggleEnum.SwitchOn
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SwitchTest {

  @Test
  fun test() = runTest(UnconfinedTestDispatcher()) {

    val stateMachine = StateMachine(initialState = On) {
      state<On> {
        on<Toggle> { transition(Off) }
      }

      state<Off> {
        on<Toggle> { transition(On) }
      }
    }

    assertEquals(expected = On, stateMachine.state.value)

    stateMachine.process(Toggle)
    assertEquals(expected = Off, stateMachine.state.value)

    stateMachine.process(Toggle)
    assertEquals(expected = On, stateMachine.state.value)

    stateMachine.cancel()
  }

  @Test
  fun enumTest() = runTest(UnconfinedTestDispatcher()) {

    val stateMachine = StateMachine(initialState = On) {
      state<On> {
        on<ToggleEnum>(guard = { action == SwitchOff }) {
          transition(Off)
        }
      }
      state<Off> {
        on<ToggleEnum>(guard = { action == SwitchOn }) {
          transition(On)
        }
      }
    }

    assertEquals(expected = On, stateMachine.state.value)

    stateMachine.process(SwitchOff)
    assertEquals(expected = Off, stateMachine.state.value)

    stateMachine.process(SwitchOn)
    assertEquals(expected = On, stateMachine.state.value)

    stateMachine.cancel()
  }
}

private sealed interface SwitchState {
  object On : SwitchState
  object Off : SwitchState
}

typealias Toggle = Unit

private enum class ToggleEnum { SwitchOn, SwitchOff }
