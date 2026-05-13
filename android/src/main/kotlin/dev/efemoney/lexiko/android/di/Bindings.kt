package dev.efemoney.lexiko.android.di

import dev.efemoney.lexiko.di.AppScope
import dev.efemoney.lexiko.util.Dispatchers
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlin.coroutines.CoroutineContext

@ContributesBinding(AppScope::class)
internal class DispatchersImpl @Inject constructor() : Dispatchers {
  override val io: CoroutineContext = kotlinx.coroutines.Dispatchers.IO
  override val main: CoroutineContext = kotlinx.coroutines.Dispatchers.Main
  override val computation: CoroutineContext = kotlinx.coroutines.Dispatchers.Default
}
