package dev.efemoney.lexiko.internal

interface RetainedScope : CoroutineScope

interface Dispatchers {
  val main: CoroutineDispatcher
  val network: CoroutineDispatcher
}
