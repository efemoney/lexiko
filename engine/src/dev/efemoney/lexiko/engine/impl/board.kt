package dev.efemoney.lexiko.engine.impl

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import dev.efemoney.lexiko.engine.api.MutableBoard
import dev.efemoney.lexiko.engine.api.TileMultiplier

internal class BoardImpl() : MutableBoard {

  private val slots: TileSlots = TileSlots(rows = 15, cols = 15) { position ->
    TileSlot(position, TileMultiplier.forPosition(position))
  }

  private val filledSlots by derivedStateOf {
    slots.map { it.tile != null }
  }

  override fun get(row: Int, col: Int) = slots[row, col]
}
