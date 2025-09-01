package dev.efemoney.lexiko.presentation.circuit.internal

import androidx.compose.ui.platform.AndroidUiDispatcher
import kotlin.coroutines.CoroutineContext

actual val PresentationContext: CoroutineContext = AndroidUiDispatcher.Main
