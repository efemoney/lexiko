package dev.efemoney.lexiko.engine.api

import androidx.compose.runtime.Stable
import kotlin.jvm.JvmInline

@Stable
interface Board {
  operator fun get(row: Int, col: Int): TileSlot
  operator fun get(position: TilePosition): TileSlot = get(position.row, position.col)
}

interface MutableBoard : Board {
  override operator fun get(row: Int, col: Int): MutableTileSlot
  override operator fun get(position: TilePosition): MutableTileSlot = get(position.row, position.col)
}

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
value class TilePlacement private constructor(private val tiles: Map<TilePosition, Tile>) {

  constructor(build: Builder.() -> Unit) : this(buildMap { Builder(this).apply(build) })

  operator fun get(position: TilePosition) = tiles[position]

  @JvmInline
  value class Builder internal constructor(private val map: MutableMap<TilePosition, Tile>) {

    infix fun Tile.at(position: TilePosition) = map.put(position, this)
  }

  companion object {
    val None = TilePlacement(emptyMap())
  }
}
