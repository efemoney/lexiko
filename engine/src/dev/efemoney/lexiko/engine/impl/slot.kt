package dev.efemoney.lexiko.engine.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.efemoney.lexiko.engine.api.Direction
import dev.efemoney.lexiko.engine.api.MutableTileSlot
import dev.efemoney.lexiko.engine.api.OnConflict
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileMultiplier
import dev.efemoney.lexiko.engine.api.TilePosition

internal class TileSlots(
  val rows: Int,
  val cols: Int,
  initSlot: (position: TilePosition) -> TileSlot,
) : Array2d<TileSlot>, Iterable<TileSlot> {

  private val array by lazy {
    check(rows > 0)
    check(cols > 0)
    Array(rows * cols) {
      initSlot(
        TilePosition(
          row = it / rows,
          col = it % rows
        )
      )
    }
  }

  override fun get(row: Int, col: Int) = array[checkIndex(row, col)]

  override fun iterator() = array.iterator()

  private fun checkIndex(row: Int, col: Int): Int {
    check(row in 0 until rows)
    check(col in 0 until cols)
    return row * cols + col
  }
}

internal class TileSlot(
  override val position: TilePosition,
  override val multiplier: TileMultiplier?,
  initialTile: Tile? = null,
) : MutableTileSlot {

  override var tile by mutableStateOf(initialTile)

  internal fun put(tile: Tile, onConflict: OnConflict = OnConflict.DoNothing) {
    this.tile?.let { onConflict(this, it, tile) }
    this.tile = tile
  }

  context(BoardImpl)
  internal fun next(direction: Direction, count: Int = 1) = get(position.next(direction, count))

  context(BoardImpl)
  internal fun prev(direction: Direction, count: Int = 1) = get(position.prev(direction, count))

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is TileSlot) return false
    return position == other.position && multiplier == other.multiplier
  }

  override fun hashCode() = 31 * position.hashCode() + multiplier.hashCode()
}

internal interface Array2d<T> {
  operator fun get(row: Int, col: Int): T
}
