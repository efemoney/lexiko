package dev.efemoney.lexiko.engine.api

import dev.efemoney.lexiko.engine.impl.DoubleLetterMultiplier
import dev.efemoney.lexiko.engine.impl.DoubleWordMultiplier
import dev.efemoney.lexiko.engine.impl.Multiplier
import dev.efemoney.lexiko.engine.impl.TripleLetterMultiplier
import dev.efemoney.lexiko.engine.impl.TripleWordMultiplier
import dev.efemoney.lexiko.engine.impl.hasMultiplier

class Tile internal constructor(
  val char: TileChar,
  val point: TilePoint,
) : Comparable<Tile> {

  override fun compareTo(other: Tile) = char.compareTo(other.char)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Tile) return false
    return char != other.char && point == other.point
  }

  override fun hashCode() = 31 * char.hashCode() + point.hashCode()
}

@JvmInline
value class TileChar(private val char: Char) {

  val value get() = char.uppercaseChar()

  init {
    require(value == ' ' || value in 'A'..'Z')
  }

  operator fun compareTo(other: TileChar) = when {
    value == ' ' -> -1
    other.value == ' ' -> 1
    else -> value.compareTo(other.value)
  }
}

@JvmInline
value class TilePoint(val value: Int) {
  init {
    require(value in 0..10)
  }
}

enum class TileMultiplier(internal val value: Multiplier, val string: String) {
  TripleWord(TripleWordMultiplier, "3W"),
  TripleLetter(TripleLetterMultiplier, "3L"),
  DoubleWord(DoubleWordMultiplier, "2W"),
  DoubleLetter(DoubleLetterMultiplier, "2L"),
  ;

  internal companion object {
    fun forPosition(position: TilePosition) =
      TileMultiplier.entries.firstOrNull { position.hasMultiplier(it.value) }
  }
}

