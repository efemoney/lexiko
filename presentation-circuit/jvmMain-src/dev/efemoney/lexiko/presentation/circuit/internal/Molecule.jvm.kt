package dev.efemoney.lexiko.presentation.circuit.internal

import androidx.compose.runtime.DefaultMonotonicFrameClock
import kotlin.coroutines.CoroutineContext

actual val PresentationContext: CoroutineContext = DefaultMonotonicFrameClock
