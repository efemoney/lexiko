package dev.efemoney.lexiko.util

import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.retain.RetainObserver

class Observers {
}

/**
 * [androidx.compose.runtime.RememberObserver] that allows for overriding only the methods of interest.
 */
abstract class RememberObserverAdapter : RememberObserver {
  override fun onRemembered() = Unit
  override fun onForgotten() = Unit
  override fun onAbandoned() = Unit
}

/**
 * [androidx.compose.runtime.retain.RetainObserver] that allows for overriding only the methods of interest.
 */
abstract class RetainObserverAdapter : RetainObserver {
  override fun onEnteredComposition() = Unit
  override fun onExitedComposition() = Unit
  override fun onRetained() = Unit
  override fun onRetired() = Unit
  override fun onUnused() = Unit
}
