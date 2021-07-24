@file:Suppress("FunctionName")

package dev.efemoney.lexiko.statemachine

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty

internal operator fun <T> StateFlow<T>.getValue(ref: Any?, p: KProperty<*>): T = value
internal operator fun <T> MutableStateFlow<T>.setValue(ref: Any?, p: KProperty<*>, newValue: T) {
  value = newValue
}

// </editor-fold>
