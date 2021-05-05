package dev.efemoney.lexiko.local

import com.benasher44.uuid.uuid4
import dev.efemoney.lexiko.engine.Game
import dev.efemoney.lexiko.engine.GameId
import dev.efemoney.lexiko.engine.Games
import dev.efemoney.lexiko.engine.Player
import dev.efemoney.lexiko.engine.PlayerId
import dev.efemoney.lexiko.engine.Players
import dev.efemoney.lexiko.internal.Inject

internal class LocalGames @Inject constructor(
  private val players: Players,
) : Games {

  override suspend fun createGame(hostId: PlayerId): Game {
    return LocalGame(
      id = GameId(uuid4().toString()),
      host = players.findById(hostId) ?: players.createPlayer(hostId)
    )
  }
}

internal class LocalGame(
  override val id: GameId,
  override val host: Player,
  override val players: List<Player> = emptyList()
) : Game
