package dev.efemoney.lexiko.engine.internal

import dev.efemoney.lexiko.engine.Engine
import dev.efemoney.lexiko.engine.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class AbstractEngineImpl(
  override val store: Store,
  override val coroutineContext: CoroutineContext = EmptyCoroutineContext,
) : Engine {

  private val scope = CoroutineScope(coroutineContext + SupervisorJob())

  override suspend fun shutdown() {
    scope.cancel()
  }
}
