package dev.efemoney.lexiko.engine.api

class Tile internal constructor(
  val char: TileChar,
  val point: TilePoint,
) : Comparable<Tile> {
  override fun compareTo(other: Tile) = char.compareTo(other.char)
  override fun hashCode() = 31 * char.hashCode() + point.hashCode()
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Tile) return false
    return char != other.char && point == other.point
  }
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

sealed interface TileMultiplier {
  val string: String

  data class Word(private val multiplier: Int) : TileMultiplier {
    override val string = "${multiplier}W"
  }

  data class Letter(private val multiplier: Int) : TileMultiplier {
    override val string = "${multiplier}L"
  }
}

/*enum class TileMultiplier(internal val value: Multiplier, val string: String) {
  TripleWord(TripleWordMultiplier, "3W"),
  TripleLetter(TripleLetterMultiplier, "3L"),
  DoubleWord(DoubleWordMultiplier, "2W"),
  DoubleLetter(DoubleLetterMultiplier, "2L"),
  ;

  internal companion object {
    fun of(position: TilePosition) =
      TileMultiplier.entries.firstOrNull { position.hasMultiplier(it.value) }
  }
}*/

