package dev.efemoney.lexiko

import kotlinx.coroutines.test.runBlockingTest

internal actual fun <T> runTest(block: suspend () -> T) {
  runBlockingTest { block() }
}
