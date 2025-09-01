package dev.efemoney.lexiko.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

interface Dispatchers {
  val io: CoroutineContext
  val main: CoroutineContext
  val computation: CoroutineContext
}

/** [runCatching] but throws cancellation so as to not interfere with normal coroutine execution */
inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
  return try {
    Result.success(block())
  } catch (c: CancellationException) {
    throw c
  } catch (e: Throwable) {
    Result.failure(e)
  }
}

suspend inline operator fun <T> CoroutineContext.invoke(noinline block: suspend CoroutineScope.() -> T): T =
  withContext(this, block)
