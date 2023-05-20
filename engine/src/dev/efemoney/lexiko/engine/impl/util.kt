@file:Suppress("NOTHING_TO_INLINE")

package dev.efemoney.lexiko.engine.impl

/**
 * Packs two Int values into one Long value for use in inline classes.
 */
inline fun packInts(val1: Int, val2: Int): Long {
  return val1.toLong().shl(32) or (val2.toLong() and 0xFFFFFFFF)
}

/**
 * Unpacks the first Int value in [packInts] from its returned ULong.
 */
inline fun unpackInt1(value: Long): Int {
  return value.shr(32).toInt()
}

/**
 * Unpacks the second Int value in [packInts] from its returned ULong.
 */
inline fun unpackInt2(value: Long): Int {
  return value.and(0xFFFFFFFF).toInt()
}

// region Lexiko

inline fun Int.requireIn(range: IntRange): Int {
  require(this in range)
  return this
}

// endregion
