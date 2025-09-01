package dev.efemoney.lexiko.engine.api

interface Game {
  val board: Board
  val players: List<Player>
}
