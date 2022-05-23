package dev.efemoney.lexiko.engine

import kotlin.coroutines.CoroutineContext

interface Engine {
  val store: Store
  val coroutineContext: CoroutineContext

  suspend fun shutdown()
}
