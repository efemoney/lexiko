package dev.efemoney.lexiko.app.navigation

import dev.efemoney.lexiko.app.internal.Retained
import dev.efemoney.lexiko.navigation.Direction
import dev.efemoney.lexiko.navigation.Navigator
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@Retained
internal class RealNavigator @Inject constructor() : Navigator {

  val directions = Channel<Direction>()

  override fun navigate(direction: Direction) {
    directions.trySend(direction)
  }
}
