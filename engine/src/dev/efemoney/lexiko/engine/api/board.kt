package dev.efemoney.lexiko.engine.api

import androidx.compose.runtime.Stable
import dev.efemoney.lexiko.engine.impl.BoardImpl

@Stable
interface Board {
  operator fun get(row: Int, col: Int): TileSlot
  operator fun get(position: TilePosition): TileSlot = get(position.row, position.col)

  companion object {
    val TopLeft = TilePosition(0, 0)
    val Center = TilePosition(7, 7)
    val BottomRight = TilePosition(14, 14)
  }
}

interface MutableBoard : Board {
  override operator fun get(row: Int, col: Int): MutableTileSlot
  override operator fun get(position: TilePosition): MutableTileSlot = get(position.row, position.col)
}

fun Board(tilePlacement: TilePlacement = TilePlacement.None): Board =
  BoardImpl(initialTilePlacement = tilePlacement)

fun Board(placeTiles: MutableBoard.() -> Unit): Board =
  BoardImpl().also(placeTiles)

context(MutableBoard)
fun Tile.placeAt(position: TilePosition) = internal.place(tile = this, at = position)


@Stable
interface TileSlot {
  val position: TilePosition
  val multiplier: TileMultiplier?
  val tile: Tile?
}

interface MutableTileSlot : TileSlot {
  override var tile: Tile?
}

operator fun TileSlot.component1() = position

operator fun TileSlot.component2() = multiplier

operator fun TileSlot.component3() = tile


@JvmInline
value class TilePlacement private constructor(private val tiles: Map<TilePosition, Tile> = emptyMap()) {

  constructor(build: Builder.() -> Unit) : this(buildMap { Builder(this).apply(build) })

  operator fun get(position: TilePosition) = tiles[position]

  @JvmInline
  value class Builder internal constructor(private val map: MutableMap<TilePosition, Tile>) {

    infix fun Tile.at(position: TilePosition) = map.put(position, this)
  }

  companion object {
    val None = TilePlacement()
  }
}

// region Internal

internal interface BoardInternal : MutableBoard {
  fun place(tile: Tile, at: TilePosition)
  fun place(tiles: List<Tile>, startAt: TilePosition, direction: Direction)
}

internal fun interface OnConflict {
  operator fun invoke(slot: TileSlot, old: Tile, new: Tile)

  companion object {
    val DoNothing = OnConflict { _, _, _ -> }
  }
}

internal val MutableBoard.internal get() = this as BoardInternal

// endregion
