package dev.efemoney.lexiko.app.internal

import androidx.lifecycle.viewModelScope
import dev.efemoney.lexiko.internal.Dispatchers
import dev.efemoney.lexiko.internal.ForegroundScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers as CoroutineDispatchers

internal class RealForegroundScope @Inject constructor(component: ForegroundComponent) :
  ForegroundScope, CoroutineScope by component.viewModelScope

internal class RealDispatchers @Inject constructor() : Dispatchers {
  override val main get() = CoroutineDispatchers.Main
  override val network get() = CoroutineDispatchers.Default
}
