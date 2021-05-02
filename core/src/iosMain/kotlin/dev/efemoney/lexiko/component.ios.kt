package dev.efemoney.lexiko

import dev.efemoney.lexiko.engine.api.Games
import dev.efemoney.lexiko.engine.api.Players
import io.ktor.client.*

actual interface CoreComponent {

  actual val httpClient: HttpClient

  actual val games: Games

  actual val players: Players
}
