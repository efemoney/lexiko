@file:OptIn(ReadOnlyGraphApi::class)

package dev.efemoney.lexiko.app.di

import androidx.compose.runtime.annotation.RememberInComposition
import dev.efemoney.lexiko.di.AppScope
import dev.efemoney.lexiko.di.BackgroundScope
import dev.efemoney.lexiko.di.ForegroundScope
import dev.efemoney.lexiko.di.ReadOnlyGraphApi
import dev.efemoney.lexiko.di.ScreenScope
import dev.efemoney.lexiko.presentation.Screen
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory

@RememberInComposition
fun AppGraph(): AppGraph = createGraphFactory<AppGraph.Factory>().create()


@DependencyGraph(AppScope::class, [BackgroundScope::class])
interface AppGraph : AppScope.Graph, BackgroundScope.Graph {

  val foreground: ForegroundGraph.Factory

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(): AppGraph
  }
}

@GraphExtension(ForegroundScope::class)
interface ForegroundGraph : ForegroundScope.Graph {

  val screen: ScreenGraph.Factory

  @GraphExtension.Factory
  fun interface Factory {
    fun create(): ForegroundGraph
  }
}

@GraphExtension(ScreenScope::class)
interface ScreenGraph : ScreenScope.Graph {

  @GraphExtension.Factory
  fun interface Factory {
    fun create(@Provides screen: Screen): ScreenGraph
  }
}
