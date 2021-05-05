package dev.efemoney.lexiko.engine

import dev.efemoney.lexiko.engine.internal.Array2d

interface Board : Array2d<TileSlot>

interface TileSlot {
  val position: Position
  val multiplier: TileMultiplier
  val tile: Tile?
}

interface Position {
  val row: Int
  val col: Int
}

interface Tile {
  val letter: TileLetter
  val value: TileValue
}

enum class TileMultiplier {
  None,
  DoubleWord,
  DoubleLetter,
  TripleWord,
  TripleLetter,
}

interface TileBag {
  val remainingTileCount: Int
  fun pickRandomTile(): Tile?
}

inline class TileLetter(val value: Char)

inline class TileValue(val value: Int)
