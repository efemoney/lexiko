package dev.efemoney.lexiko.engine.handle

import dev.efemoney.lexiko.engine.GameContext
import dev.efemoney.lexiko.engine.GameEvent
import me.tatarka.inject.annotations.Inject
import kotlin.reflect.KType

internal fun interface GameEventHandler<in E : GameEvent> {
  context(GameContext) suspend fun handle(event: E)
}

@Inject
internal class GameEventHandlers(
  handlersMapping: Map<KType, () -> GameEventHandler<GameEvent>>
) : GameEventHandler<GameEvent> {

  private val handlersMapping =
    handlersMapping.entries.associate { it.key.classifier to lazy(it.value) }

  context(GameContext) override suspend fun handle(event: GameEvent) =
    handlersMapping[event::class]!!.value.handle(event)
}
