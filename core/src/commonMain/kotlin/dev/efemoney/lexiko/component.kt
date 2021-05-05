package dev.efemoney.lexiko

import dev.efemoney.lexiko.engine.Games
import dev.efemoney.lexiko.engine.Players
import io.ktor.client.*

/**
 * Component representing dependency graph
 */
expect interface CoreComponent {

  val games: Games

  val players: Players

  val httpClient: HttpClient
}
