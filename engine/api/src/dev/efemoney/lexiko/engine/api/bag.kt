package dev.efemoney.lexiko.engine.api

import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastForEach

@Stable
interface BagOfTiles {
  val remainingTilesCount: Int
  val isEmpty get() = remainingTilesCount == 0

  fun pickRandomTile(): Tile?
  fun pickRandomTiles(max: Int): List<Tile> =
    if (isEmpty) emptyList() else buildList { for (i in 0..<max) add(pickRandomTile() ?: break) }

  fun returnTile(tile: Tile)
  fun returnTiles(tiles: List<Tile>) = tiles.fastForEach(::returnTile)
}

interface InitialBagOfTiles : BagOfTiles {
  fun pickTile(char: TileChar): Tile?
  fun pickTiles(chars: List<TileChar>): List<Tile> =
    chars.mapNotNull(::pickTile).also { require(it.size == chars.size) { "Some tiles are not available" } }
}
