package dev.efemoney.lexiko.engine.api

import androidx.compose.runtime.Stable

@Stable
interface Board {
  operator fun get(row: Int, col: Int): TileSlot
  operator fun get(position: TilePosition): TileSlot = get(position.row, position.col)

  fun place(
    word: Word,
    startAt: TilePosition,
    inDirection: WordDirection
  )

  companion object {
    val TopLeft = TilePosition(0, 0)
    val Center = TilePosition(7, 7)
    val BottomRight = TilePosition(14, 14)
  }
}

interface MutableBoard : Board {
  override operator fun get(row: Int, col: Int): MutableTileSlot
  override operator fun get(position: TilePosition): MutableTileSlot = get(position.row, position.col)
}

@Stable
interface TileSlot {
  val position: TilePosition
  val multiplier: TileMultiplier?
  val tile: Tile?
}

@Stable
interface MutableTileSlot : TileSlot {
  override var tile: Tile?
}
