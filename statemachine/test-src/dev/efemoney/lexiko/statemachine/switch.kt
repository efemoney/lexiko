package dev.efemoney.lexiko.statemachine

import dev.efemoney.lexiko.statemachine.SwitchState.Off
import dev.efemoney.lexiko.statemachine.SwitchState.On
import dev.efemoney.lexiko.statemachine.ToggleEnum.SwitchOff
import dev.efemoney.lexiko.statemachine.ToggleEnum.SwitchOn
import dev.efemoney.lexiko.statemachine.dsl.StateMachine
import dev.efemoney.lexiko.statemachine.dsl.on
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SwitchTest {

  @Test
  fun test() = runTest(UnconfinedTestDispatcher()) {

    val stateMachine = StateMachine<SwitchState, Toggle>(initialState = On) {

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
  fun enumActionsTest() = runTest(UnconfinedTestDispatcher()) {

    val stateMachine = StateMachine(initialState = On) {

      state<On> {
        on(SwitchOff) {
          transition(Off)
        }
      }

      state<Off> {
        on(SwitchOn) {
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
  data object On : SwitchState
  data object Off : SwitchState
}

typealias Toggle = Unit

private enum class ToggleEnum { SwitchOn, SwitchOff }
