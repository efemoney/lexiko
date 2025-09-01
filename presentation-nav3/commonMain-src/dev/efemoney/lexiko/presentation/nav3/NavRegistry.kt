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
import androidx.compose.ui.util.fastForEach
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.SceneSetupNavEntryDecorator
import dev.efemoney.lexiko.presentation.Screen

class NavRegistry private constructor(builder: Builder) {

  internal val entryProviders = entryProvider(
    fallback = {
      NavEntry(
        key = it,
        content = builder.unavailableScreen,
      )
    }
  ) {
    builder.entryProviders.fastForEach {
      it.invoke(this)
    }
  }

  internal val entryDecorators = buildList {
    add(SceneSetupNavEntryDecorator())
    addAll(builder.entryDecorators)
  }

  class Builder() {

    internal val unavailableScreen: @Composable (Screen) -> Unit =
      DefaultUnavailableScreen

    internal val entryProviders: MutableList<EntryProviderBuilder<Screen>.() -> Unit> =
      mutableListOf()

    internal val entryDecorators: MutableList<NavEntryDecorator<Screen>> =
      mutableListOf()

    @RememberInComposition
    fun build(): NavRegistry = NavRegistry(this)
  }
}

private val DefaultUnavailableScreen = @Composable { screen: Screen ->
  val color = LocalNavAnimatedContentScope
  BasicText(
    modifier = Modifier.background(Color.Black, RoundedCornerShape(4.dp)),
    style = TextStyle(Color.White),
    text = when (LocalInspectionMode.current) {
      true -> "[${screen::class.simpleName}]"
      else -> "Page not available: $screen"
    },
  )
}
