package dev.efemoney.lexiko.engine.api

import dev.efemoney.lexiko.engine.impl.Direction

@JvmInline
value class Word(val tiles: List<Tile>) : List<Tile> by tiles {
  constructor(vararg tiles: Tile) : this(tiles.toList())
}

enum class WordDirection(internal val dir: Direction) {
  Horizontal(Direction.Horizontal),
  Vertical(Direction.Vertical),
  ;
}

@JvmInline
value class Letter(val tile: Tile)
