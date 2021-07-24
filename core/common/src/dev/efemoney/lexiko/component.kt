package dev.efemoney.lexiko

import io.ktor.client.*

/**
 * Component representing dependency graph
 */
expect interface CoreComponent {

  val httpClient: HttpClient
}
