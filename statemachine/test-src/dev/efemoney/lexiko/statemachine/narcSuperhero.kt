package dev.efemoney.lexiko.statemachine

import app.cash.turbine.test
import dev.efemoney.lexiko.statemachine.NarcHeroEvent.DistressCall
import dev.efemoney.lexiko.statemachine.NarcHeroEvent.WakeUp
import dev.efemoney.lexiko.statemachine.NarcHeroEvent.WorkOut
import dev.efemoney.lexiko.statemachine.NarcHeroState.Asleep
import dev.efemoney.lexiko.statemachine.NarcHeroState.HangingOut
import dev.efemoney.lexiko.statemachine.NarcHeroState.Hungry
import dev.efemoney.lexiko.statemachine.NarcHeroState.SavingTheWorld
import dev.efemoney.lexiko.statemachine.dsl.StateMachine
import dev.efemoney.lexiko.statemachine.dsl.on
import dev.efemoney.lexiko.statemachine.dsl.onAny
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class NarcolepticSuperheroTest {

  @Test
  fun run() = runTest(UnconfinedTestDispatcher()) {

    val batman = StateMachine {

      state<Asleep> {
        on(WakeUp) { transition(HangingOut) }
      }

      state<HangingOut> {
        on(WorkOut) { transition(Hungry) }
      }

      onAny(DistressCall) { transition(SavingTheWorld) }

      state<SavingTheWorld> {
        onEnter { changeIntoSecretCostume() }
        onExit { updateJournal() }
      }
    }

    batman.state.test {

    }
  }

  private fun updateJournal() = println("Dear Diary, today I saved Mr. Whiskers. Again.")

  private fun changeIntoSecretCostume() = println("Beauty, eh?")
}

sealed interface NarcHeroState {
  object Asleep : NarcHeroState
  object HangingOut : NarcHeroState
  object Hungry : NarcHeroState
  object Sweaty : NarcHeroState
  object SavingTheWorld : NarcHeroState
}

enum class NarcHeroEvent {
  WakeUp, WorkOut, Eat, DistressCall, CompleteMission, CleanUp, Nap
}
