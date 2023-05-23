package dev.efemoney.lexiko.engine

import dev.efemoney.lexiko.engine.handle.HandlersComponent
import me.tatarka.inject.annotations.Component

@Component
internal abstract class EngineComponent(
  @Component val handlers: HandlersComponent,
) {
  abstract val createGame: () -> Game
}
