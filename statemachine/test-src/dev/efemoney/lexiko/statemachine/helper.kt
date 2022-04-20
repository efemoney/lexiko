package dev.efemoney.lexiko.statemachine

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.time.Duration

fun TestScope.advanceTimeBy(duration: Duration) = advanceTimeBy(duration.inWholeMilliseconds)
