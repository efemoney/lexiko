package dev.efemoney.lexiko.statemachine.internal

sealed interface Return<T>

internal class ReturnT<T>(val state: T) : Return<T>

internal object ReturnNothing : Return<Nothing>

