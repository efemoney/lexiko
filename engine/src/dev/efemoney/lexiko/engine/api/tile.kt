package dev.efemoney.lexiko.engine.api

import dev.efemoney.lexiko.engine.impl.DoubleLetterMultiplier
import dev.efemoney.lexiko.engine.impl.DoubleWordMultiplier
import dev.efemoney.lexiko.engine.impl.Multiplier
import dev.efemoney.lexiko.engine.impl.TripleLetterMultiplier
import dev.efemoney.lexiko.engine.impl.TripleWordMultiplier
import dev.efemoney.lexiko.engine.impl.hasMultiplier

data class Tile(
  val char: TileChar,
  val point: TilePoint,
) : Comparable<Tile> {
  override fun compareTo(other: Tile) = char.compareTo(other.char)
}

@JvmInline
value class TileChar(val value: Char) {

  init {
    require(value == ' ' || value in 'A'..'Z')
  }

  operator fun compareTo(other: TileChar) = when {
    this == Space -> -1
    other == Space -> 1
    else -> value.compareTo(other.value)
  }

  companion object {
    val Space = TileChar(' ')
  }
}

@JvmInline
value class TilePoint(val value: Int) {
  init {
    require(value in 0..10)
  }
}

enum class TileMultiplier(internal val value: Multiplier) {
  TripleWord(TripleWordMultiplier),
  TripleLetter(TripleLetterMultiplier),
  DoubleWord(DoubleWordMultiplier),
  DoubleLetter(DoubleLetterMultiplier),
  None(Multiplier.None),
  ;

  internal companion object {
    fun forPosition(position: TilePosition) =
      TileMultiplier.entries.first { position.hasMultiplier(it.value) }
  }
}

