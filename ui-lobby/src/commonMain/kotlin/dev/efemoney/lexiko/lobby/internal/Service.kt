package dev.efemoney.lexiko.lobby.internal

import dev.efemoney.lexiko.util.Dispatchers
import dev.efemoney.lexiko.util.invoke
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Inject
internal class LobbyService(
  private val dispatchers: Dispatchers,
) {
  suspend fun request(): String = dispatchers.io {
    delay(3.seconds)
    ""
  }
}
