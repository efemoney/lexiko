package dev.efemoney.lexiko

import com.hypercubetools.ktor.moshi.moshi
import io.ktor.client.features.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentNegotiation
import org.slf4j.event.Level

fun Application.plugins() {
  install(AutoHeadResponse)
  install(CORS)
  install(Compression) { gzip() }
  install(CallLogging) { level = Level.INFO }
  install(ContentNegotiation) { moshi(component.moshi) }
}
