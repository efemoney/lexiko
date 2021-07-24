package dev.efemoney.lexiko

import dev.efemoney.lexiko.engine.Games
import dev.efemoney.lexiko.engine.Players
import io.ktor.client.*

actual interface CoreComponent {

  actual val httpClient: HttpClient

  actual val games: Games

  actual val players: Players
}
