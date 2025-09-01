package dev.efemoney.lexiko.presentation.nav3

import androidx.navigation3.runtime.NavEntry
import com.sun.jndi.toolkit.url.Uri
import dev.efemoney.lexiko.di.AppScope
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.ScreenDefinition
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@SingleIn(AppScope::class)
@Inject
class EntryProviders(
  private val providers: Map<Uri, ScreenDefinition<*>>,
) {
  inline fun <reified T : Screen> provideEntry(screen: T): NavEntry<T> =
    provideEntry(typeOf<T>(), screen)

  @PublishedApi
  internal fun <T : Screen> provideEntry(type: KType, screen: T): NavEntry<T> {

    TODO()
  }
}
