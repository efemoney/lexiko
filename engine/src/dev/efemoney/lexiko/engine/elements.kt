@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine

interface Board

interface TileSlot {
  val position: Position
  val multiplier: Multiplier
  val tile: Tile?
}

interface Position {
  val row: Int
  val col: Int
}

enum class Multiplier {
  None,
  DoubleWord,
  DoubleLetter,
  TripleWord,
  TripleLetter,
}

interface Tile {
  val char: TileChar
  val point: TilePoint
}

interface TileChar {
  val value: Char
}

interface TilePoint {
  val value: Int
}

interface TileBag {
  val remainingTilesCount: Int
  fun pickRandomTiles(count: Int): List<Tile>
}

inline fun TileBag.pickRandomTile(): Tile? = pickRandomTiles(1).firstOrNull()

inline fun TileBag.isEmpty() = remainingTilesCount == 0
