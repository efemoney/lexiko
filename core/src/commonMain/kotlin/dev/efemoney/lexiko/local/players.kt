package dev.efemoney.lexiko.local

import dev.efemoney.lexiko.engine.Player
import dev.efemoney.lexiko.engine.PlayerId
import dev.efemoney.lexiko.engine.Players
import dev.efemoney.lexiko.internal.Inject

internal class LocalPlayers @Inject constructor() : Players {

  private val players = mutableListOf<Player>()

  constructor(players: List<Player>) : this() {
    this.players += players
  }

  override suspend fun findById(playerId: PlayerId) = players.find { it.id == playerId }

  override suspend fun createPlayer(playerId: PlayerId) = LocalPlayer(playerId).also(players::add)
}

internal class LocalPlayer(override val id: PlayerId) : Player
