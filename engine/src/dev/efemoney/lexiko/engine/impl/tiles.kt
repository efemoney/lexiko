package dev.efemoney.lexiko.engine.impl

import dev.efemoney.lexiko.engine.api.Tile
import dev.efemoney.lexiko.engine.api.TileChar
import dev.efemoney.lexiko.engine.api.TilePoint

internal val Tiles = mapOf(
  0 to " ",
  1 to "A,E,I,L,N,O,R,S,T,U",
  2 to "D,G",
  3 to "B,C,M,P",
  4 to "F,H,V,W,Y",
  5 to "K",
  8 to "J,X",
  10 to "Q,Z",
).flatMap { (point, chars) ->
  chars.split(',').map {
    Tile(
      TileChar(it.single()),
      TilePoint(point),
    )
  }
}
