package dev.efemoney.lexiko

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Application.routes() = routing {
  route("/ws") {
    webSocket {

    }
  }
}
