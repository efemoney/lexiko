package dev.efemoney.lexiko.statemachine

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration

class StateMachineTest {

  @Test
  fun stateMachineTest() = runBlockingTest {

    val stateMachine = StateMachineOf(initialState = Ice) {

      state<Solid> {
        accept<Melt>()
      }

      state<Liquid> {
        accept<Densify>()
        accept<Vaporize>()
      }

      state<Gas> {
        accept<Densify>()
      }
    }

    assertEquals(Ice, stateMachine.state)
    assertTrue { stateMachine.process(Melt) }
    assertEquals(Ice, stateMachine.state) // should be ice because we are still melting

    advanceTimeBy(30.seconds.inWholeMilliseconds) // after melting is complete
    assertEquals(Water, stateMachine.state) // we should be completely melted

    assertTrue { stateMachine.process(Vaporize) }
    advanceTimeBy(60.seconds.inWholeMilliseconds)
    assertEquals(Steam, stateMachine.state)
  }
}

val Ice = Solid(timeToMelt = 20.seconds)
val Water = Liquid(timeToSolidify = Ice.timeToMelt, timeToVaporize = 60.seconds)
val Steam = Gas(timeToLiquefy = Water.timeToVaporize)

sealed interface StateOfMatter
data class Solid(val timeToMelt: Duration) : StateOfMatter
data class Liquid(val timeToSolidify: Duration, val timeToVaporize: Duration) : StateOfMatter
data class Gas(val timeToLiquefy: Duration) : StateOfMatter

sealed interface Mattermorphosis { // get it?
  suspend fun morph(state: StateOfMatter): StateOfMatter
}

object Densify : Mattermorphosis {
  override suspend fun morph(state: StateOfMatter): StateOfMatter {
    when (state) {
      is Gas -> delay(state.timeToLiquefy)
      is Liquid -> delay(state.timeToSolidify)
      else -> error("")
    }
    return when (state) {
      Steam -> Water
      Water -> Ice
      else -> error("")
    }
  }
}

object Melt : Mattermorphosis {
  override suspend fun morph(state: StateOfMatter): StateOfMatter {
    when (state) {
      is Solid -> delay(state.timeToMelt)
      else -> error("")
    }
    return Water
  }
}

object Vaporize : Mattermorphosis {
  override suspend fun morph(state: StateOfMatter): StateOfMatter {
    when (state) {
      is Liquid -> delay(state.timeToVaporize)
      else -> error("")
    }
    return Steam
  }
}

private inline val Int.seconds get() = Duration.seconds(this)

private inline fun <reified T : Mattermorphosis> StateTransitionBuilder<out StateOfMatter, Mattermorphosis>.accept() =
  on<T> {
    it.morph(state)
  }
