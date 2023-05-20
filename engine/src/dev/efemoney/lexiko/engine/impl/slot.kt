package dev.efemoney.lexiko.engine.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.efemoney.lexiko.engine.api.MutableTileSlot
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileMultiplier
import dev.efemoney.lexiko.engine.api.TilePosition
import dev.efemoney.lexiko.engine.internal.Array2d

internal class TileSlots(
  override val rows: Int,
  override val cols: Int,
  initSlot: (position: TilePosition) -> TileSlot,
) : Array2d<TileSlot>, Iterable<TileSlot> {

  private val array by lazy {
    check(rows > 0)
    check(cols > 0)
    Array(rows * cols) {
      initSlot(TilePosition(row = it / rows, col = it % rows))
    }
  }

  override fun get(row: Int, col: Int) = array[checkRowAndCol(row, col)]

  override fun iterator() = array.iterator()

  private fun checkRowAndCol(row: Int, col: Int): Int {
    check(row in 0 until rows)
    check(col in 0 until cols)
    return row * cols + col
  }
}

internal class TileSlot(
  override val position: TilePosition,
  override val multiplier: TileMultiplier,
) : MutableTileSlot {

  override var tile by mutableStateOf<Tile?>(null)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is TileSlot) return false
    return position == other.position && multiplier == other.multiplier
  }

  override fun hashCode() = 31 * position.hashCode() + multiplier.hashCode()
}

internal enum class Direction { Horizontal, Vertical }
