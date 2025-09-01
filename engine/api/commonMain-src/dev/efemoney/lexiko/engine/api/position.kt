@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine.api

import androidx.compose.ui.util.packInts
import androidx.compose.ui.util.unpackInt1
import androidx.compose.ui.util.unpackInt2
import kotlin.jvm.JvmInline

@JvmInline
value class TilePosition private constructor(@InternalEngineApi val packed: Long) {

  constructor(row: Int, col: Int) : this(
    packInts(
      val1 = row.requireIn(Range) { "Row[$row] is not in range $Range" },
      val2 = col.requireIn(Range) { "Col[$col] is not in range $Range" },
    )
  )

  val row get() = unpackInt1(packed)
  val col get() = unpackInt2(packed)

  inline fun next(orientation: Orientation, count: Int = 1) = when (orientation) {
    Orientation.Vertical -> TilePosition(row + count, col)
    Orientation.Horizontal -> TilePosition(row, col + count)
  }

  inline fun hasNext(orientation: Orientation, count: Int = 1) = when (orientation) {
    Orientation.Vertical -> row + count
    Orientation.Horizontal -> col + count
  } <= Range.last

  inline fun prev(orientation: Orientation, count: Int = 1) = when (orientation) {
    Orientation.Vertical -> TilePosition(row - count, col)
    Orientation.Horizontal -> TilePosition(row, col - count)
  }

  inline fun hasPrev(orientation: Orientation, count: Int = 1) = when (orientation) {
    Orientation.Vertical -> row - count
    Orientation.Horizontal -> col - count
  } >= Range.first

  override fun toString() = "TilePosition[$row, $col]"

  companion object {
    val Range = 0..14
  }
}

enum class Orientation { Horizontal, Vertical }

fun positions(from: TilePosition, orientation: Orientation): Sequence<TilePosition> =
  generateSequence(from) { if (it.hasNext(orientation)) it.next(orientation) else null }

fun positions(from: TilePosition, to: TilePosition): Sequence<TilePosition> {
  val orientation = when {
    from == to -> return sequenceOf(from)
    from.row == to.row -> Orientation.Horizontal
    from.col == to.col -> Orientation.Vertical
    else -> error("Cannot find a path from: $from - to: $to")
  }
  return generateSequence(from) { if (it == to) null else it.next(orientation) }
}
