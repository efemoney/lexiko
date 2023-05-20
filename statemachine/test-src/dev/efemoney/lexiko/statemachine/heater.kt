package dev.efemoney.lexiko.statemachine

import app.cash.turbine.test
import dev.efemoney.lexiko.statemachine.dsl.StateMachine
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.time.Duration

class HeaterTest {

  @Test @Disabled
  fun test() = runTest {

    val heaterOnMachine = StateMachine(HeaterRunning) {

      state<HeaterRunning> {
        on<Sleep> {
          transition(HeaterSleeping(event.duration))
        }
      }

      state<HeaterSleeping> {
        onEnter {
          delay(state.duration)
          emit(Wake)
        }
        on<Wake> {
          transition(HeaterRunning)
        }
      }
    }

    val stateMachine = StateMachine(HeaterOff) {

      initialState<HeaterOn>()

      state<HeaterOn>(heaterOnMachine) {
        on<TurnOff> {
          noTransition()
        }
      }

      state<HeaterOff> {
        on<TurnOn> {
          noTransition()
        }
      }
    }

    stateMachine.state.test {

    }
  }
}

private sealed interface HeaterState
private data object HeaterOff : HeaterState

private sealed interface HeaterOn : HeaterState
private data object HeaterRunning : HeaterOn
private data class HeaterSleeping(val duration: Duration = Duration.INFINITE) : HeaterOn


private sealed interface HeaterEvent
private data object TurnOn : HeaterEvent
private data object TurnOff : HeaterEvent
private data class Sleep(val duration: Duration) : HeaterEvent
private data object Wake : HeaterEvent
