package dev.efemoney.lexiko.engine

import dev.efemoney.lexiko.engine.internal.Array2d
import kotlin.test.Test
import kotlin.test.assertEquals

class Array2dTest {

  @Test
  fun testArray2dCreation() {
    val array2d = Array2d(15, 15, ::Pair)

    assertEquals(Pair(0, 1), array2d[0, 1])
    assertEquals(Pair(12, 7), array2d[12, 7])
  }
}
