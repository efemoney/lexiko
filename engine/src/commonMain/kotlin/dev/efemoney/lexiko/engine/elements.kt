package dev.efemoney.lexiko.engine

import dev.efemoney.lexiko.engine.internal.Array2d

interface Board : Array2d<TileSlot>

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
  val letter: TileLetter
  val point: TilePoint
}

interface TileLetter {
  val value: Char
}

interface TilePoint {
  val value: Int
}

interface TileBag {
  val remainingTilesCount: Int
  fun pickRandomTile(): Tile?
}

inline fun TileBag.isEmpty() = remainingTilesCount == 0
