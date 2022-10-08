@file:Suppress(
  "SpellCheckingInspection",
  "NOTHING_TO_INLINE",
  "CANNOT_OVERRIDE_INVISIBLE_MEMBER",
  "INVISIBLE_MEMBER",
  "INVISIBLE_REFERENCE",
)

package dev.efemoney.lexiko.engine.impl

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.efemoney.lexiko.engine.api.BagOfTiles
import dev.efemoney.lexiko.engine.api.InitialBagOfTiles
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileChar
import dev.efemoney.lexiko.engine.api.TilePoint

internal val TileCounts = listOf(
  1 to "JKQXZ",
  2 to "BCFHMPVWY ",
  3 to "G",
  4 to "DLSU",
  6 to "NRT",
  8 to "O",
  9 to "AI",
  12 to "E",
).flatMap { (count, chars) ->
  chars.map { TileChar(it) to count }
}.toMap()

internal val TilePoints = listOf(
  0 to " ",
  1 to "AEILNORSTU",
  2 to "DG",
  3 to "BCMP",
  4 to "FHVWY",
  5 to "K",
  8 to "JX",
  10 to "QZ",
).flatMap { (point, chars) ->
  chars.map { TileChar(it) to point }
}.toMap()

internal inline fun pointOf(char: TileChar) = TilePoint(TilePoints[char]!!)
internal inline fun countOf(char: TileChar) = TileCounts[char]!!
internal inline fun countOf(tile: Tile) = countOf(tile.char)

internal class DefaultBagOfTiles : BagOfTiles, InitialBagOfTiles {

  private val tiles = sequence { yieldAll('A'..'Z'); yield(' ') }
    .map(::TileChar)
    .associateWith { char ->
      SnapshotStateList<Tile>().apply {
        repeat(countOf(char)) {
          add(Tile(char, pointOf(char)))
        }
      }
    }

  private val remainingChars by derivedStateOf {
    buildSet {
      tiles.forEach { (char, remaining) ->
        if (remaining.isNotEmpty()) add(char)
      }
    }
  }

  override val remainingTilesCount by derivedStateOf {
    tiles.values.sumOf { it.size }
  }

  override fun pickRandomTile(): Tile? {
    return if (isEmpty) null else pick(remainingChars.random())
  }

  override fun returnTile(tile: Tile) {
    tilesOf(tile).run {
      check(size + 1 <= countOf(tile))
      add(tile)
    }
  }

  override fun pickTile(char: TileChar): Tile? {
    return if (char in remainingChars) pick(char) else null
  }

  override fun pickTiles(chars: List<TileChar>): List<Tile> {
    TODO("Not yet implemented")
  }

  override fun toString(): String = buildString {
    append("Bag(")
    remainingChars.joinTo(this) { "$it=${tilesOf(it).size}" }
    append(')')
  }

  internal inline fun pick(char: TileChar): Tile {
    require(char in remainingChars)
    return tilesOf(char).removeLast()
  }

  internal inline fun pick(chars: CharSequence): List<Tile> = chars.map { pick(TileChar(it)) }

  private inline fun tilesOf(char: TileChar) = tiles[char]!!

  private inline fun tilesOf(tile: Tile) = tilesOf(tile.char)
}
