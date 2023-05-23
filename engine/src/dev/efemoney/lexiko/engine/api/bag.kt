@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine.api

interface BagOfTiles {
  val remainingTilesCount: Int
  fun pickRandomTiles(count: Int): List<Tile>
}

inline fun BagOfTiles.pickRandomTile(): Tile? = pickRandomTiles(1).firstOrNull()

inline fun BagOfTiles.isEmpty() = remainingTilesCount == 0
