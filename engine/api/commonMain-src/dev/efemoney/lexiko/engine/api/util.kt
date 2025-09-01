package dev.efemoney.lexiko.engine.api

internal inline fun Int.requireIn(range: IntRange, lazyMessage: () -> Any): Int {
  require(this in range, lazyMessage)
  return this
}
