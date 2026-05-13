package dev.efemoney.lexiko.presentation.di

import androidx.navigation3.runtime.NavEntryDecorator
import dev.efemoney.lexiko.di.ForegroundScope
import dev.efemoney.lexiko.di.ScreenScope
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.nav3.NavEntryProvider
import dev.efemoney.lexiko.presentation.nav3.NavRegistry
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides

@ContributesTo(ForegroundScope::class)
interface NavBindings {

  @Multibinds(allowEmpty = true)
  val entryProviders: Set<NavEntryProvider>

  @Multibinds(allowEmpty = true)
  val entryDecorators: Set<NavEntryDecorator<Screen>>

  @Provides
  fun registry(graphFactory: (Screen) -> ScreenScope.Graph): NavRegistry {
    return NavRegistry.Builder(graphFactory)
      .entryProviders(entryProviders.toList())
      .entryDecorators(entryDecorators.toList())
      .build()
  }
}
