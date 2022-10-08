package dev.efemoney.lexiko.app.navigation

import androidx.navigation.NavDirections
import dev.efemoney.lexiko.app.internal.Retained
import dev.efemoney.lexiko.navigation.Navigator
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@Retained
internal class RealNavigator @Inject constructor() : Navigator {

  val directions = Channel<NavDirections>()

  override fun navigate(direction: NavDirections) {
    directions.trySend(direction)
  }
}
