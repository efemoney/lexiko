package dev.efemoney.lexiko.engine.events

import dev.efemoney.lexiko.engine.GameContext
import me.tatarka.inject.annotations.Inject
import kotlin.reflect.KType

sealed interface GameEvent

internal fun interface GameEventHandler<in E : GameEvent> {
  context(GameContext) suspend fun handle(event: E)
}

@Inject
internal class GameEventHandlers(eventHandlerMappings: Map<KType, () -> GameEventHandler<GameEvent>>) {

  private val eventHandlerMappings =
    eventHandlerMappings.entries.associate { it.key.classifier!! to lazy(it.value) }

  context(GameContext) suspend fun handle(event: GameEvent) = eventHandlerMappings[event::class]!!.value.handle(event)
}
