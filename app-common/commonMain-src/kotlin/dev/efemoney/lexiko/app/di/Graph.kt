package dev.efemoney.lexiko.app.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface AppGraph {

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(): AppGraph
  }
}
