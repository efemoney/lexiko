package dev.efemoney.lexiko

import io.ktor.client.*

actual interface LexikoComponent {

  actual val httpClient: HttpClient
}
