package dev.efemoney.lexiko.engine.di

import dev.efemoney.lexiko.engine.Game
import dev.efemoney.lexiko.engine.events.GameEvent
import dev.efemoney.lexiko.engine.events.GameEventHandler
import dev.efemoney.lexiko.engine.events.StartGameEvent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Component
@Suppress("UNCHECKED_CAST")
internal interface EventHandlersComponent {
  val handlersMapping: Map<KType, () -> GameEventHandler<GameEvent>>

  @Provides @IntoMap
  fun (() -> StartGameEvent.Handler).provide() =
    typeOf<StartGameEvent>() to (this as () -> GameEventHandler<GameEvent>)
}

@Component
internal abstract class EngineComponent(
  @Component val eventHandlers: EventHandlersComponent,
) {
  abstract val newGame: () -> Game
}
