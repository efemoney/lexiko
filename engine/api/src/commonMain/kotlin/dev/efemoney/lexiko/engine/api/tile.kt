package dev.efemoney.lexiko.engine.api

import androidx.collection.buildIntSet
import dev.drewhamilton.poko.Poko
import kotlin.jvm.JvmInline

@Poko
class Tile @InternalEngineApi constructor(
  val char: TileChar,
  val point: TilePoint,
) : Comparable<Tile> {
  override fun compareTo(other: Tile) = char.compareTo(other.char)
  override fun toString() = "Tile('${char.value}')"
}

@JvmInline
value class TileChar(val code: Int) {
  init {
    require(code in TileCharCodes)
  }

  val value get() = code.toChar()

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
  val multiplier: Int

  @Poko
  private class Word(override val multiplier: Int) : TileMultiplier {
    override val string get() = "${multiplier}W"
  }

  @Poko
  private class Letter(override val multiplier: Int) : TileMultiplier {
    override val string get() = "${multiplier}L"
  }

  companion object {
    val TripleWord: TileMultiplier = Word(3)
    val DoubleWord: TileMultiplier = Word(2)
    val TripleLetter: TileMultiplier = Letter(3)
    val DoubleLetter: TileMultiplier = Letter(2)
  }
}

@InternalEngineApi
val TileCharCodes = buildIntSet {
  for (char in 'A'..'Z') add(char.code)
  add(' '.code)
}
