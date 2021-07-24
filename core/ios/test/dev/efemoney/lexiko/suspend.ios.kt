package dev.efemoney.lexiko

import kotlinx.coroutines.runBlocking

internal actual fun <T> runTest(block: suspend () -> T) {
  runBlocking { block() }
}
