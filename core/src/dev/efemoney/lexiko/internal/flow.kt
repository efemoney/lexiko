@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.internal

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty

internal inline operator fun <T> StateFlow<T>.getValue(scope: Any, prop: KProperty<*>): T = value

internal inline operator fun <T> MutableStateFlow<T>.setValue(scope: Any, prop: KProperty<*>, value: T) {
  this.value = value
}
