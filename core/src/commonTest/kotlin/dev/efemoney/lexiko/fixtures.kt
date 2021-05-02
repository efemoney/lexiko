package dev.efemoney.lexiko

import dev.efemoney.lexiko.engine.api.Player
import dev.efemoney.lexiko.engine.api.PlayerId
import dev.efemoney.lexiko.local.LocalPlayer
import dev.efemoney.lexiko.local.LocalPlayers

internal object TestPlayers {
  operator fun invoke(vararg players: Player) = LocalPlayers(players.toList())
  operator fun invoke(playerId: PlayerId) = invoke(LocalPlayer(playerId))
}
