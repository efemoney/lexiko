package dev.efemoney.lexiko.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

interface ForegroundScope : CoroutineScope

interface Dispatchers {
  val main: CoroutineDispatcher
  val network: CoroutineDispatcher
}
