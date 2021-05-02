package dev.efemoney.lexiko.local

import dev.efemoney.lexiko.engine.api.Player
import dev.efemoney.lexiko.engine.api.PlayerId
import dev.efemoney.lexiko.engine.api.Players
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
