package dev.efemoney.lexiko.lobby

import dev.efemoney.lexiko.util.Dispatchers
import dev.efemoney.lexiko.util.invoke
import dev.zacsweers.metro.Inject

@Inject
internal class LobbyService(
  private val dispatchers: Dispatchers,
) {
  suspend fun request() = dispatchers.io {

  }
}
