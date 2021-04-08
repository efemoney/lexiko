package dev.efemoney.lexiko

import io.ktor.client.*

/**
 * Component representing dependency graph
 */
expect interface LexikoComponent {

  val httpClient: HttpClient
}
