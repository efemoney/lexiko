@file:Suppress("CANDIDATE_CHOSEN_USING_OVERLOAD_RESOLUTION_BY_LAMBDA_ANNOTATION")

package dev.efemoney.lexiko.statemachine

import dev.efemoney.lexiko.statemachine.dsl.StateMachine
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.time.Duration

class HeaterTest {

  @Test @Disabled
  fun test() = runTest {

    val child = StateMachine {

      initialState(HeaterRunning)

      state<HeaterOn> {
        on<TurnOff> {
          transition(TODO())
        }
      }

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
      }
    }

    val stateMachine = StateMachine {

      nestedState(child)

      state<HeaterOff> {
        on<TurnOn> {
          transition(TODO())
        }
      }
    }

    stateMachine
  }
}

private sealed interface HeaterState
private interface HeaterOff : HeaterState
private sealed interface HeaterOn : HeaterState
private object HeaterRunning : HeaterOn
private data class HeaterSleeping(val duration: Duration = Duration.INFINITE) : HeaterOn


private sealed interface HeaterEvent
private object TurnOn : HeaterEvent
private object TurnOff : HeaterEvent
private data class Sleep(val duration: Duration) : HeaterEvent
private object Wake : HeaterEvent
