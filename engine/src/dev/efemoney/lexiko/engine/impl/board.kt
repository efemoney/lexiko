package dev.efemoney.lexiko.engine.impl

import dev.efemoney.lexiko.engine.api.MutableBoard
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileMultiplier
import dev.efemoney.lexiko.engine.api.TilePosition
import dev.efemoney.lexiko.engine.api.Word
import dev.efemoney.lexiko.engine.api.WordDirection

internal class BoardImpl(
  initialTilePlacement: TilePlacement = TilePlacement.None,
) : MutableBoard {

  private val slots = TileSlots(rows = 15, cols = 15) { position ->
    TileSlot(
      position = position,
      multiplier = TileMultiplier.forPosition(position),
      initialTile = initialTilePlacement.tiles[position],
    )
  }

  override fun get(row: Int, col: Int) = slots[row, col]

  override fun get(position: TilePosition) = get(position.row, position.col)

  override fun place(word: Word, startAt: TilePosition, inDirection: WordDirection) {
    word.asSequence()
      .zip(generateSequence(get(startAt)) { it.next(inDirection.dir) })
      .forEach { (tile, slot) ->
        slot.tile = tile
      }
  }

  override fun toString() = buildString {
    appendLine("Board(")
    for (row in 0..<slots.rows) {
      for (col in 0..<slots.cols) {
        slots[row, col].tile.run {
          append(if (this == null) '.' else char.value)
        }
        append(' ')
      }
      appendLine()
    }
    append(')')
  }
}

@JvmInline
value class TilePlacement private constructor(internal val tiles: Map<TilePosition, Tile>) {

  constructor(build: Builder.() -> Unit) : this(buildMap { Builder(this).apply(build) })

  @JvmInline
  value class Builder internal constructor(
    private val map: MutableMap<TilePosition, Tile>
  ) : MutableMap<TilePosition, Tile> by map {

    infix fun Tile.at(position: TilePosition) = map.put(position, this)
  }

  companion object {
    val None = TilePlacement(emptyMap())
  }
}
