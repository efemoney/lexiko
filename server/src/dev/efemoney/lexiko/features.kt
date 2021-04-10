package dev.efemoney.lexiko

import com.hypercubetools.ktor.moshi.moshi
import io.ktor.application.*
import io.ktor.features.*
import org.slf4j.event.Level

fun Application.features() {
  install(AutoHeadResponse)
  install(CORS)
  install(Compression) { gzip() }
  install(CallLogging) { level = Level.INFO }
  install(ContentNegotiation) { moshi(component.moshi) }
}
