@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine.api

import dev.efemoney.lexiko.engine.impl.Direction
import dev.efemoney.lexiko.engine.impl.packInts
import dev.efemoney.lexiko.engine.impl.requireIn
import dev.efemoney.lexiko.engine.impl.unpackInt1
import dev.efemoney.lexiko.engine.impl.unpackInt2

@JvmInline
value class TilePosition private constructor(private val packed: Long) {

  constructor(row: Int, col: Int) : this(
    packInts(
      row.requireIn(Range),
      col.requireIn(Range),
    )
  )

  val row get() = unpackInt1(packed)

  val col get() = unpackInt2(packed)

  inline operator fun component1() = row

  inline operator fun component2() = col

  internal inline fun next(direction: Direction) = when (direction) {
    Direction.Vertical -> TilePosition((row + 1).coerceAtMost(MaxIndex), col)
    Direction.Horizontal -> TilePosition(row, (col + 1).coerceAtMost(MaxIndex))
  }

  internal inline fun prev(direction: Direction) = when (direction) {
    Direction.Vertical -> TilePosition((row - 1).coerceAtLeast(MinIndex), col)
    Direction.Horizontal -> TilePosition(row, (col - 1).coerceAtLeast(MinIndex))
  }

  companion object {
    const val MinIndex = 0
    const val MaxIndex = 14
    internal val Range = MinIndex..MaxIndex
    internal val Center = TilePosition(7, 7)
  }
}
