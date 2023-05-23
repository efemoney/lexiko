@file:Suppress("SpellCheckingInspection", "NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine.impl

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import dev.efemoney.lexiko.engine.api.BagOfTiles
import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileChar
import dev.efemoney.lexiko.engine.api.TilePoint

private val AtoZ = 'A'..'Z'
private const val Space = ' '

internal val TilesCount = listOf(
  1 to "JKQXZ",
  2 to " BCFHMPVWY",
  3 to "G",
  4 to "DLSU",
  6 to "NRT",
  8 to "O",
  9 to "AI",
  12 to "E",
).flatMap { (count, chars) ->
  chars.map { it to count }
}.toMap()

internal val TilesPoint = listOf(
  0 to " ",
  1 to "AEILNORSTU",
  2 to "DG",
  3 to "BCMP",
  4 to "FHVWY",
  5 to "K",
  8 to "JX",
  10 to "QZ",
).flatMap { (point, chars) ->
  chars.map { it to point }
}.toMap()

internal class BagOfTilesImpl : BagOfTiles {

  private val tiles = buildMap {
    val fillMap = { char: Char ->
      val count = TilesCount[char]!!
      val point = TilesPoint[char]!!
      val tile = Tile(TileChar(char), TilePoint(point))
      put(
        key = char,
        value = List(count) { tile }.toMutableStateList(),
      )
    }
    AtoZ.forEach(fillMap)
    fillMap(Space)
  }

  private val remainingChars by derivedStateOf {
    buildSet {
      tiles.forEach {
        if (it.value.isNotEmpty()) add(it.key)
      }
    }
  }

  override val remainingTilesCount by derivedStateOf {
    tiles.values.sumOf { it.size }
  }

  override fun pickRandomTiles(count: Int): List<Tile> {
    check(count <= remainingTilesCount)
    return List(count) { pick(remainingChars.random()) }
  }

  override fun toString(): String = buildString {
    append("Bag(")
    remainingChars.joinTo(this) { "$it=${tilesOf(it).size}" }
    append(')')
  }

  internal inline fun pick(char: Char): Tile = tilesOf(char.uppercaseChar()).removeLast()

  internal inline fun pick(chars: String): List<Tile> = pick(chars = chars.toCharArray())

  internal inline fun pick(vararg chars: Char): List<Tile> = chars.map(::pick)

  private inline fun tilesOf(char: Char): SnapshotStateList<Tile> {
    require(char in remainingChars)
    return tiles[char]!!
  }
}
