package dev.efemoney.lexiko.engine.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.drewhamilton.poko.Poko
import dev.efemoney.lexiko.engine.api.MutableTileSlot
import dev.efemoney.lexiko.engine.api.OnConflict
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileMultiplier
import dev.efemoney.lexiko.engine.api.TilePosition

internal class DefaultTileSlots(
  private val rows: Int,
  private val cols: Int,
  initSlot: (position: TilePosition) -> DefaultTileSlot,
) : Array2d<DefaultTileSlot>, Iterable<DefaultTileSlot> {

  private val array by lazy {
    check(rows > 0)
    check(cols > 0)
    Array(rows * cols) { initSlot(TilePosition(row = it / rows, col = it % rows)) }
  }

  override fun get(row: Int, col: Int) = array[checkIndex(row, col)]

  override fun iterator() = array.iterator()

  private fun checkIndex(row: Int, col: Int): Int {
    check(row in 0..<rows)
    check(col in 0..<cols)
    return row * cols + col
  }
}

@Poko
internal class DefaultTileSlot(
  override val position: TilePosition,
  override val multiplier: TileMultiplier?,
  initialTile: Tile? = null,
) : MutableTileSlot {

  override var tile by mutableStateOf(initialTile)

  internal fun put(tile: Tile, onConflict: OnConflict = OnConflict.DoNothing) {
    this.tile?.let { onConflict(this, it, tile) }
    this.tile = tile
  }
}

internal interface Array2d<T> {
  operator fun get(row: Int, col: Int): T
}
