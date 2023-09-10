@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine.api

import dev.efemoney.lexiko.engine.impl.packInts
import dev.efemoney.lexiko.engine.impl.requireIn
import dev.efemoney.lexiko.engine.impl.unpackInt1
import dev.efemoney.lexiko.engine.impl.unpackInt2

@JvmInline
value class TilePosition private constructor(private val packed: Long) {

  constructor(row: Int, col: Int) : this(
    packInts(
      row.requireIn(Range) { "Row[$row] is not in range $Range" },
      col.requireIn(Range) { "Row[$col] is not in range $Range" },
    )
  )

  val row get() = unpackInt1(packed)

  val col get() = unpackInt2(packed)

  inline fun next(direction: Direction, count: Int = 1) = when (direction) {
    Direction.Vertical -> TilePosition(row + count, col)
    Direction.Horizontal -> TilePosition(row, col + count)
  }

  inline fun prev(direction: Direction, count: Int = 1) = when (direction) {
    Direction.Vertical -> TilePosition(row - count, col)
    Direction.Horizontal -> TilePosition(row, col - count)
  }

  companion object {
    val Range = 0..14
  }
}

enum class Direction { Horizontal, Vertical }

fun positions(from: TilePosition, to: TilePosition): Sequence<TilePosition> {
  val direction = when {
    from == to -> return sequenceOf(from)
    from.row == to.row -> Direction.Horizontal
    from.col == to.col -> Direction.Vertical
    else -> error("Cannot find a path from: $from - to: $to")
  }
  return generateSequence(from) { if (it == to) null else it.next(direction) }
}

fun positions(from: TilePosition, inDirection: Direction): Sequence<TilePosition> =
  generateSequence(from) { it.next(inDirection) }
