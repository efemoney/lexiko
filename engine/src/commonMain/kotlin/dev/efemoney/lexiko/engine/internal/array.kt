@file:Suppress("FunctionName")

package dev.efemoney.lexiko.engine.internal

@InternalEngineApi
interface Array2d<T> {
  val rows: Int
  val cols: Int
  operator fun get(row: Int, col: Int): T
}

internal interface MutableArray2d<T> : Array2d<T> {
  operator fun set(row: Int, col: Int, value: T)
}

internal fun <T> Array2d(rows: Int, cols: Int, initializer: (Int, Int) -> T): Array2d<T> {
  return FakeArray2d(rows, cols, initializer)
}

internal class FakeArray2d<T>(
  override val rows: Int,
  override val cols: Int,
  initializer: (Int, Int) -> T,
) : Array2d<T> {

  private val delegate: Array<Slot<T>>

  init {
    check(rows > 0)
    check(cols > 0)
    delegate = Array(rows * cols) {
      val row = it / rows
      val col = it % rows
      Slot(row, col, initializer(row, col))
    }
  }

  override fun get(row: Int, col: Int): T {
    checkRowAndCol(row, col)
    return delegate[row * cols + col].value
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as FakeArray2d<*>

    if (rows != other.rows) return false
    if (cols != other.cols) return false
    if (!delegate.contentEquals(other.delegate)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = rows
    result = 31 * result + cols
    result = 31 * result + delegate.contentHashCode()
    return result
  }

  private fun checkRowAndCol(row: Int, col: Int) {
    check(row in 0 until rows)
    check(col in 0 until cols)
  }

  private data class Slot<T>(val row: Int, val col: Int, val value: T)
}
