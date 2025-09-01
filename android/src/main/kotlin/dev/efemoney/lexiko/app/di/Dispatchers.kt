package dev.efemoney.lexiko.app.di

import dev.efemoney.lexiko.di.AppScope
import dev.efemoney.lexiko.util.Dispatchers
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class DispatchersImpl @Inject constructor(
  override val io: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.IO,
  override val main: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.Main,
  override val computation: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.Default,
) : Dispatchers
