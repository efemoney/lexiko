package dev.efemoney.lexiko

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level

fun Application.plugins() {
  install(AutoHeadResponse)
  install(CORS)
  install(Compression) { gzip() }
  install(ContentNegotiation)
  install(CallLogging) { level = Level.INFO }
  install(WebSockets)
}
