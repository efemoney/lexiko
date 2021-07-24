package dev.efemoney.lexiko

internal expect fun <T> runTest(block: suspend () -> T)
