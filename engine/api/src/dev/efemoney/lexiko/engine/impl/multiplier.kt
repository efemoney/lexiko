package dev.efemoney.lexiko.engine.impl

import dev.efemoney.lexiko.engine.api.Letter
import dev.efemoney.lexiko.engine.api.TilePosition
import dev.efemoney.lexiko.engine.api.Word

internal sealed interface Multiplier {
  fun TilePosition.hasThisMultiplier(): Boolean
}

internal abstract class LetterMultiplier(private val factor: Float) : Multiplier {
  fun apply(letter: Letter) = Unit
}

internal abstract class WordMultiplier(private val factor: Float) : Multiplier {
  fun apply(word: Word) = Unit
}

internal val TripleWordMultiplier = object : WordMultiplier(3.0f) {
  private val indices = setOf(0, 7, 14)
  override fun TilePosition.hasThisMultiplier() = row in indices && col in indices
}

internal val TripleLetterMultiplier = object : LetterMultiplier(3.0f) {
  override fun TilePosition.hasThisMultiplier(): Boolean {
    return true
  }
}

internal val DoubleWordMultiplier = object : WordMultiplier(2.0f) {
  override fun TilePosition.hasThisMultiplier(): Boolean {
    return true
  }
}

internal val DoubleLetterMultiplier = object : LetterMultiplier(2.0f) {
  override fun TilePosition.hasThisMultiplier(): Boolean {
    return true
  }
}

internal fun TilePosition.hasMultiplier(multiplier: Multiplier) = with(multiplier) { hasThisMultiplier() }

