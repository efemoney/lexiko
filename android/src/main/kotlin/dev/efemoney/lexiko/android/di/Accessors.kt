package dev.efemoney.lexiko.android.di

import dev.efemoney.lexiko.android.LexikoActivity
import dev.efemoney.lexiko.di.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
internal interface ComponentAccessors {
  val lexikoActivity: LexikoActivity
}
