package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.annotation.RememberInComposition
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import dev.efemoney.lexiko.di.ScreenScope
import dev.efemoney.lexiko.presentation.Screen

class NavRegistry private constructor(builder: Builder) {

  internal val entryProviders = entryProvider<Screen>(
    fallback = { NavEntry(key = it, content = builder.unavailableScreen) },
    builder = {
      for (provider in builder.entryProviders) {
      }
    },
  )

  internal val entryDecorators = builder.entryDecorators

  class Builder(
    private val graphFactory: (Screen) -> ScreenScope.Graph,
  ) {
    internal val entryProviders = mutableListOf<NavEntryProvider>()
    internal val entryDecorators = mutableListOf<NavEntryDecorator<Screen>>()
    internal var unavailableScreen = DefaultUnavailableScreen

    fun entryProvider(provider: NavEntryProvider) = apply { entryProviders.add(provider) }

    fun entryProviders(providers: List<NavEntryProvider>) = apply { entryProviders.addAll(providers) }

    fun entryDecorator(decorator: NavEntryDecorator<Screen>) = apply { entryDecorators.add(decorator) }

    fun entryDecorators(decorators: List<NavEntryDecorator<Screen>>) = apply { entryDecorators.addAll(decorators) }

    fun unavailableScreen(screen: @Composable (Screen) -> Unit) = apply { unavailableScreen = screen }

    @RememberInComposition
    fun build(): NavRegistry = NavRegistry(this)
  }
}

private val DefaultUnavailableScreen = @Composable { screen: Screen ->
  BasicText(
    modifier = Modifier.background(Color.Black, RoundedCornerShape(4.dp)),
    style = TextStyle(Color.White),
    text = when (LocalInspectionMode.current) {
      true -> "[${screen::class.simpleName}]"
      else -> "Page unavailable: $screen"
    },
  )
}
