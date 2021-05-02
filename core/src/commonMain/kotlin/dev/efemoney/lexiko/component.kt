package dev.efemoney.lexiko

import dev.efemoney.lexiko.engine.api.Games
import dev.efemoney.lexiko.engine.api.Players
import io.ktor.client.*

/**
 * Component representing dependency graph
 */
expect interface CoreComponent {

  val games: Games

  val players: Players

  val httpClient: HttpClient
}
