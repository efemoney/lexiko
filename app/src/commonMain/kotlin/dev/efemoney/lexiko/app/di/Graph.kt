package dev.efemoney.lexiko.app.di

import androidx.compose.runtime.annotation.RememberInComposition
import dev.efemoney.lexiko.di.AppScope
import dev.efemoney.lexiko.di.BackgroundScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.createGraphFactory
import dev.efemoney.lexiko.di.AppGraph as CommonAppGraph

@DependencyGraph(
  scope = AppScope::class,
  additionalScopes = [BackgroundScope::class],
)
interface AppGraph : CommonAppGraph {

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(): AppGraph
  }

  companion object {
    @RememberInComposition
    operator fun invoke(): AppGraph = createGraphFactory<Factory>().create()
  }
}
