package dev.efemoney.lexiko.engine.api

import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastForEach

@Stable
interface BagOfTiles {
  /**
   * The count of remaining tiles in this bag.
   */
  val remainingTilesCount: Int

  /**
   * Whether this bag is empty.
   */
  val isEmpty get() = remainingTilesCount == 0

  /**
   * Pick a tile at random.
   *
   * @return the tile or null if this bag is empty.
   */
  fun pickRandomTile(): Tile?

  /**
   * Pick a list of tiles at random.
   *
   * @param max the maximum number of tiles to pick.
   * @return a list of tiles or an empty list if this bag is empty.
   * the size of the returned list may be smaller than [max]
   */
  fun pickRandomTiles(max: Int): List<Tile> = when {
    isEmpty -> emptyList()
    else -> generateSequence(::pickRandomTile).take(max).toList()
  }

  /**
   * Return a tile to this bag.
   */
  fun returnTile(tile: Tile)

  /**
   * Return a list of tiles to the bag.
   */
  fun returnTiles(tiles: List<Tile>) = tiles.fastForEach(::returnTile)
}
