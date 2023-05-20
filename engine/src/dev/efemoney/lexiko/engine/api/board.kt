@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine.api

import androidx.compose.runtime.Stable

interface Board {
  operator fun get(row: Int, col: Int): TileSlot
  operator fun get(position: TilePosition) = get(position.row, position.col)
}

interface MutableBoard : Board {
  override operator fun get(row: Int, col: Int): MutableTileSlot
  override operator fun get(position: TilePosition) = get(position.row, position.col)
}

@Stable
interface TileSlot {
  val position: TilePosition
  val multiplier: TileMultiplier
  val tile: Tile?
}

@Stable
interface MutableTileSlot : TileSlot {
  override var tile: Tile?
}

interface TileBag {
  val remainingTilesCount: Int
  fun pickRandomTiles(count: Int): List<Tile>
}

inline fun TileBag.pickRandomTile(): Tile? = pickRandomTiles(1).firstOrNull()

inline fun TileBag.isEmpty() = remainingTilesCount == 0
