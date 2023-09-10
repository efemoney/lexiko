package dev.efemoney.lexiko.engine.api

import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastForEach
import dev.efemoney.lexiko.engine.impl.BagOfTilesImpl

@Stable
interface BagOfTiles {
  val remainingTilesCount: Int
  val isEmpty get() = remainingTilesCount == 0
  fun pickRandomTile(): Tile?
  fun pickRandomTiles(max: Int): List<Tile>
  fun returnTile(tile: Tile)
  fun returnTiles(tiles: List<Tile>) = tiles.fastForEach(::returnTile)
}

fun BagOfTiles(): BagOfTiles = BagOfTilesImpl()
