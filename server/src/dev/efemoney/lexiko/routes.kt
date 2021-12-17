package dev.efemoney.lexiko

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Application.routes() = routing {
  route("/ws") {
    webSocket {

    }
  }
}
