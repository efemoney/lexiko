package dev.efemoney.lexiko.engine.api

@JvmInline
value class Word(private val tiles: List<Tile>) {

  constructor(vararg tiles: Tile) : this(tiles.toList())

  fun asSequence() = tiles.asSequence()
}

@JvmInline
value class Letter(private val tile: Tile)
