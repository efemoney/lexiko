package dev.efemoney.lexiko.engine.impl

import dev.efemoney.lexiko.engine.api.BoardInternal
import dev.efemoney.lexiko.engine.api.Direction
import dev.efemoney.lexiko.engine.api.OnConflict
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileMultiplier
import dev.efemoney.lexiko.engine.api.TilePlacement
import dev.efemoney.lexiko.engine.api.TilePosition
import dev.efemoney.lexiko.engine.api.component1
import dev.efemoney.lexiko.engine.api.component2
import dev.efemoney.lexiko.engine.api.component3
import dev.efemoney.lexiko.engine.api.positions

internal class BoardImpl(
  private val initialTilePlacement: TilePlacement = TilePlacement.None,
  private val onConflict: OnConflict = OnConflict.DoNothing,
) : BoardInternal {

  private val slots = TileSlots(15, 15) { position ->
    TileSlot(
      position = position,
      multiplier = TileMultiplier.of(position),
      initialTile = initialTilePlacement[position],
    )
  }

  override fun get(row: Int, col: Int) = slots[row, col]

  override fun get(position: TilePosition) = get(position.row, position.col)

  override fun place(tile: Tile, at: TilePosition) {
    get(at).put(tile, onConflict)
  }

  override fun place(tiles: List<Tile>, startAt: TilePosition, direction: Direction) {
    tiles.asSequence()
      .zip(slots(from = startAt, direction))
      .forEach { (tile, slot) -> slot.put(tile, onConflict) }
  }

  override fun toString() = buildString {
    var oldRow = -1
    append("Board(")
    for ((pos, _, tile) in slots) {
      val row = pos.row
      if (oldRow != row) appendLine()
      append(tile?.char?.value ?: '.')
      oldRow = row
    }
    append(')')
  }

  private fun slots(from: TilePosition, to: TilePosition) = positions(from, to).map(::get)

  private fun slots(from: TilePosition, inDirection: Direction) = positions(from, inDirection).map(::get)
}
