package dev.efemoney.lexiko.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

interface RetainedScope : CoroutineScope

interface Dispatchers {
  val main: CoroutineDispatcher
  val network: CoroutineDispatcher
}
