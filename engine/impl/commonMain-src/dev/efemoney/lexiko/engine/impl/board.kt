package dev.efemoney.lexiko.engine.impl

import androidx.collection.LongObjectMap
import androidx.collection.MutableLongObjectMap
import androidx.collection.buildLongObjectMap
import dev.efemoney.lexiko.engine.api.BoardInternal
import dev.efemoney.lexiko.engine.api.OnConflict
import dev.efemoney.lexiko.engine.api.Orientation
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileMultiplier
import dev.efemoney.lexiko.engine.api.TilePlacement
import dev.efemoney.lexiko.engine.api.TilePosition
import dev.efemoney.lexiko.engine.api.TileSlot
import dev.efemoney.lexiko.engine.api.component1
import dev.efemoney.lexiko.engine.api.component2
import dev.efemoney.lexiko.engine.api.component3
import dev.efemoney.lexiko.engine.api.positions

internal class DefaultBoard(
  private val initialTilePlacement: TilePlacement = TilePlacement.None,
  private val onConflict: OnConflict = OnConflict.DoNothing,
) : BoardInternal {

  private val slots = DefaultTileSlots(15, 15) { position ->
    DefaultTileSlot(
      position = position,
      multiplier = TileMultipliers[position],
      initialTile = initialTilePlacement[position],
    )
  }

  override fun get(row: Int, col: Int) = slots[row, col]

  override fun get(position: TilePosition): DefaultTileSlot = get(position.row, position.col)

  override fun place(tile: Tile, at: TilePosition) {
    get(at).put(tile, onConflict)
  }

  override fun place(tiles: List<Tile>, startAt: TilePosition, orientation: Orientation) {
    tiles
      .asSequence()
      .zip(positions(from = startAt, orientation), ::place)
  }

  internal fun TileSlot.next(orientation: Orientation, count: Int = 1) = get(position.next(orientation, count))

  internal fun TileSlot.prev(orientation: Orientation, count: Int = 1) = get(position.prev(orientation, count))

  override fun toString() = buildString {
    var oldRow = -1
    append("Board(")
    for ((pos, _, tile) in slots) {
      val row = pos.row
      if (oldRow != row) appendLine()
      append(tile?.char?.value ?: '.')
      oldRow = row
    }
    append(')')
  }
}

/** Map of (packed) tile position to tile multiplier */
internal val TileMultipliers = buildLongObjectMap {
  // Triple Word (Multiplier = 3)
  this[0, 0] = TileMultiplier.TripleWord
  this[0, 7] = TileMultiplier.TripleWord
  this[0, 14] = TileMultiplier.TripleWord
  this[7, 0] = TileMultiplier.TripleWord
  this[7, 14] = TileMultiplier.TripleWord
  this[14, 0] = TileMultiplier.TripleWord
  this[14, 7] = TileMultiplier.TripleWord
  this[14, 14] = TileMultiplier.TripleWord

  // Triple Letter (Multiplier = 3)
  this[1, 5] = TileMultiplier.TripleLetter
  this[1, 9] = TileMultiplier.TripleLetter
  this[5, 1] = TileMultiplier.TripleLetter
  this[5, 5] = TileMultiplier.TripleLetter
  this[5, 9] = TileMultiplier.TripleLetter
  this[5, 13] = TileMultiplier.TripleLetter
  this[9, 1] = TileMultiplier.TripleLetter
  this[9, 5] = TileMultiplier.TripleLetter
  this[9, 9] = TileMultiplier.TripleLetter
  this[9, 13] = TileMultiplier.TripleLetter
  this[13, 5] = TileMultiplier.TripleLetter
  this[13, 9] = TileMultiplier.TripleLetter

  // Double Word (Multiplier = 2)
  this[1, 1] = TileMultiplier.DoubleWord
  this[1, 13] = TileMultiplier.DoubleWord
  this[2, 2] = TileMultiplier.DoubleWord
  this[2, 12] = TileMultiplier.DoubleWord
  this[3, 3] = TileMultiplier.DoubleWord
  this[3, 11] = TileMultiplier.DoubleWord
  this[4, 4] = TileMultiplier.DoubleWord
  this[4, 10] = TileMultiplier.DoubleWord
  this[7, 7] = TileMultiplier.DoubleWord
  this[10, 4] = TileMultiplier.DoubleWord
  this[10, 10] = TileMultiplier.DoubleWord
  this[11, 3] = TileMultiplier.DoubleWord
  this[11, 11] = TileMultiplier.DoubleWord
  this[12, 2] = TileMultiplier.DoubleWord
  this[12, 12] = TileMultiplier.DoubleWord
  this[13, 1] = TileMultiplier.DoubleWord
  this[13, 13] = TileMultiplier.DoubleWord

  // Double Letter (Multiplier = 2)
  this[0, 3] = TileMultiplier.DoubleLetter
  this[0, 11] = TileMultiplier.DoubleLetter
  this[2, 6] = TileMultiplier.DoubleLetter
  this[2, 8] = TileMultiplier.DoubleLetter
  this[3, 0] = TileMultiplier.DoubleLetter
  this[3, 7] = TileMultiplier.DoubleLetter
  this[3, 14] = TileMultiplier.DoubleLetter
  this[6, 2] = TileMultiplier.DoubleLetter
  this[6, 6] = TileMultiplier.DoubleLetter
  this[6, 8] = TileMultiplier.DoubleLetter
  this[6, 12] = TileMultiplier.DoubleLetter
  this[7, 3] = TileMultiplier.DoubleLetter
  this[7, 11] = TileMultiplier.DoubleLetter
  this[8, 2] = TileMultiplier.DoubleLetter
  this[8, 6] = TileMultiplier.DoubleLetter
  this[8, 8] = TileMultiplier.DoubleLetter
  this[8, 12] = TileMultiplier.DoubleLetter
  this[11, 0] = TileMultiplier.DoubleLetter
  this[11, 7] = TileMultiplier.DoubleLetter
  this[11, 14] = TileMultiplier.DoubleLetter
  this[12, 6] = TileMultiplier.DoubleLetter
  this[12, 8] = TileMultiplier.DoubleLetter
  this[14, 3] = TileMultiplier.DoubleLetter
  this[14, 11] = TileMultiplier.DoubleLetter
}

private operator fun MutableLongObjectMap<TileMultiplier>.set(row: Int, col: Int, multiplier: TileMultiplier) {
  put(TilePosition(row, col).packed, multiplier)
}

private operator fun LongObjectMap<TileMultiplier>.get(position: TilePosition): TileMultiplier? =
  get(position.packed)
