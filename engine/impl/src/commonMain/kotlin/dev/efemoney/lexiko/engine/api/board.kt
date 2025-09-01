package dev.efemoney.lexiko.engine.api

import dev.efemoney.lexiko.engine.impl.DefaultBoard
import kotlin.jvm.JvmInline

fun Board(tilePlacement: TilePlacement = TilePlacement.None): Board =
  DefaultBoard(initialTilePlacement = tilePlacement)

@JvmInline
value class TilePlacement private constructor(private val placement: Map<TilePosition, Tile>) {

  constructor(build: Builder.() -> Unit) : this(buildMap { Builder(this).apply(build) })

  operator fun get(position: TilePosition) = placement[position]

  @JvmInline
  value class Builder internal constructor(private val map: MutableMap<TilePosition, Tile>) {

    infix fun Tile.at(position: TilePosition) = map.put(position, this)
  }

  companion object {
    val None = TilePlacement(emptyMap())
  }
}

// region Implementation

internal interface BoardInternal : MutableBoard {
  fun place(tile: Tile, at: TilePosition)
  fun place(tiles: List<Tile>, startAt: TilePosition, orientation: Orientation) {

  }

  fun Tile.placeAt(position: TilePosition) = place(this, at = position)
}

internal val Board.internal get() = this as BoardInternal

internal fun interface OnConflict {
  companion object {
    val DoNothing = OnConflict { _, _, _ -> }
  }

  operator fun invoke(slot: TileSlot, old: Tile, new: Tile)
}

// endregion
