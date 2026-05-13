package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.navigation3.runtime.EntryProviderScope
import dev.efemoney.lexiko.presentation.Presenter
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.Ui
import dev.efemoney.lexiko.presentation.UiState
import kotlin.reflect.typeOf

fun interface NavEntryProvider {
  fun EntryProviderScope<Screen>.invoke()
}

inline fun <reified T : Screen, S : UiState> NavEntryProvider(
  crossinline metadata: MutableMap<String, Any>.() -> Unit = {},
  crossinline presenterFactory: () -> Presenter<T, S>,
  crossinline uiFactory: () -> Ui<S>,
): NavEntryProvider = NavEntryProvider {

  addEntryProvider(
    clazz = T::class,
    clazzContentKey = { typeOf<T>() },
    metadata = buildMap(metadata),
  ) { screen ->
    val presentation = retain {
      val presenter = presenterFactory()
      Presentation {
        presenter.run(screen)
      }
    }
    remember(uiFactory).Content(presentation.state)
  }
}
