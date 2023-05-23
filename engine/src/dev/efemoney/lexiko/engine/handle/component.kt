package dev.efemoney.lexiko.engine.handle

import dev.efemoney.lexiko.engine.GameEvent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Component
internal interface HandlersComponent {
  val handlersMapping: Map<KType, () -> GameEventHandler<GameEvent>>

  @Provides @IntoMap
  fun (() -> StartGameHandler).provide() = handler()
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified T : GameEvent> (() -> GameEventHandler<T>).handler() =
  typeOf<T>() to this as () -> GameEventHandler<GameEvent> // Have to erase type to match final provided type
