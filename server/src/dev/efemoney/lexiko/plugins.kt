package dev.efemoney.lexiko

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.websocket.WebSockets
import org.slf4j.event.Level

fun Application.plugins() {
  install(AutoHeadResponse)
  install(CORS)
  install(Compression) { gzip() }
  install(ContentNegotiation)
  install(CallLogging) { level = Level.INFO }
  install(WebSockets)
}
