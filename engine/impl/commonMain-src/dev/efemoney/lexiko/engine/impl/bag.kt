@file:Suppress(
  "SpellCheckingInspection",
  "NOTHING_TO_INLINE",
)

package dev.efemoney.lexiko.engine.impl

import androidx.collection.IntObjectMap
import androidx.collection.IntSet
import androidx.collection.buildIntIntMap
import androidx.collection.buildIntObjectMap
import androidx.collection.buildIntSet
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.efemoney.lexiko.engine.api.BagOfTiles
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileChar
import dev.efemoney.lexiko.engine.api.TileCharCodes
import dev.efemoney.lexiko.engine.api.TilePoint
import kotlin.random.Random
import kotlin.collections.removeLast as removeLastKt

internal val TileCounts = listOf(
  1 to "JKQXZ",
  2 to "BCFHMPVWY ",
  3 to "G",
  4 to "DLSU",
  6 to "NRT",
  8 to "O",
  9 to "AI",
  12 to "E",
).let {
  buildIntIntMap {
    for ((count, string) in it) for (char in string) put(char.code, count)
  }
}

internal val TilePoints = listOf(
  0 to " ",
  1 to "AEILNORSTU",
  2 to "DG",
  3 to "BCMP",
  4 to "FHVWY",
  5 to "K",
  8 to "JX",
  10 to "QZ",
).let {
  buildIntIntMap {
    for ((point, string) in it) for (char in string) put(char.code, point)
  }
}

internal class DefaultBagOfTiles : BagOfTiles {

  /**
   * Map of character code to a list of remaining tiles in this bag.
   */
  private val tiles: IntObjectMap<SnapshotStateList<Tile>> = buildIntObjectMap {
    TileCharCodes.forEach { code ->
      put(code, SnapshotStateList<Tile>().apply {
        repeat(TileCounts[code]) {
          add(Tile(TileChar(code), TilePoint(TilePoints[code])))
        }
      })
    }
  }

  /**
   * The set of character codes that have remaining tiles in the bag.
   */
  private val remainingChars: IntSet by derivedStateOf {
    buildIntSet {
      tiles.forEach { code, remaining ->
        if (!remaining.isEmpty()) add(code)
      }
    }
  }

  override val remainingTilesCount: Int by derivedStateOf {
    var count = 0
    tiles.forEachValue { count += it.size }
    count
  }

  override fun pickRandomTile(): Tile? {
    return if (isEmpty) null else pick(remainingChars.random())
  }

  override fun returnTile(tile: Tile) {
    tilesOf(tile.char).run {
      check(size + 1 <= TileCounts[tile.char.code])
      add(tile)
    }
  }

  override fun toString(): String = remainingChars
    .joinToString(prefix = "Bag(", postfix = ")") { "${Char(it)}=${tilesOf(it).size}" }

  internal inline fun pick(char: TileChar): Tile = pick(char.code)

  internal inline fun pick(code: Int): Tile {
    require(code in remainingChars)
    return tilesOf(code).removeLastKt()
  }

  private inline fun tilesOf(char: TileChar) = tilesOf(char.code)

  private inline fun tilesOf(code: Int): SnapshotStateList<Tile> {
    return tiles[code]!!
  }
}

private fun IntSet.random(): TileChar {
  val index = Random.nextInt(size)
  var i = 0
  forEach { if (i++ == index) return TileChar(it) }
  error("Should not reach here")
}
