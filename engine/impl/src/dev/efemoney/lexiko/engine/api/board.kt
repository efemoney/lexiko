package dev.efemoney.lexiko.engine.api

import dev.efemoney.lexiko.engine.impl.BoardImpl

fun Board(tilePlacement: TilePlacement = TilePlacement.None): Board =
  BoardImpl(initialTilePlacement = tilePlacement)

context(InitialBagOfTiles)
fun Board(placeTiles: context(InitialBagOfTiles) MutableBoard.() -> Unit): Board {
  return BoardImpl().also { placeTiles(self, it) }
}

context(MutableBoard)
fun Tile.placeAt(position: TilePosition) = internal.place(this, at = position)

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

private val InitialBagOfTiles.self get() = this

internal interface BoardInternal : MutableBoard {
  fun place(tile: Tile, at: TilePosition)
  fun place(tiles: List<Tile>, startAt: TilePosition, direction: Direction)
}

internal val Board.internal get() = this as BoardInternal

internal fun interface OnConflict {
  operator fun invoke(slot: TileSlot, old: Tile, new: Tile)

  companion object {
    val DoNothing = OnConflict { _, _, _ -> }
  }
}

// endregion
