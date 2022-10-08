package dev.efemoney.lexiko.engine.api

@JvmInline
value class Word(private val tiles: Sequence<Tile>)

@JvmInline
value class Letter(private val tile: Tile)
