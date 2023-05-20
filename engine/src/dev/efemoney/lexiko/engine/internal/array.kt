package dev.efemoney.lexiko.engine.internal

internal interface Array2d<T> {
  val rows: Int
  val cols: Int
  operator fun get(row: Int, col: Int): T
}

internal interface MutableArray2d<T> : Array2d<T> {
  operator fun set(row: Int, col: Int, value: T)
}

internal fun <T> MutableArray2d(rows: Int, cols: Int, initializer: (Int, Int) -> T): MutableArray2d<T> =
  FlatArrayAs2d(rows, cols, initializer)

internal fun <T> MutableArray2d<T>.asArray2d() = object : Array2d<T> by this {}

private class FlatArrayAs2d<T>(
  override val rows: Int,
  override val cols: Int,
  initializer: (row: Int, col: Int) -> T,
) : MutableArray2d<T> {

  private val delegate by lazy {
    check(rows > 0)
    check(cols > 0)

    Array(rows * cols) {
      val row = it / rows
      val col = it % rows
      Slot(row, col, initializer(row, col))
    }
  }

  override fun get(row: Int, col: Int): T {
    return delegate[checkRowAndCol(row, col)].value
  }

  override fun set(row: Int, col: Int, value: T) {
    delegate[checkRowAndCol(row, col)] = Slot(row, col, value)
  }

  private fun checkRowAndCol(row: Int, col: Int): Int {
    check(row in 0 until rows)
    check(col in 0 until cols)
    return row * cols + col
  }
}

private class Slot<T>(val row: Int, val col: Int, val value: T)
