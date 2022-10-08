package dev.efemoney.lexiko.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.efemoney.lexiko.engine.api.BagOfTiles
import dev.efemoney.lexiko.engine.api.Board
import dev.efemoney.lexiko.engine.api.Direction
import dev.efemoney.lexiko.engine.api.placeAt

@Composable
fun Board(boardState: BoardState, modifier: Modifier = Modifier) {
  Column(
    verticalArrangement = Arrangement.spacedBy(1.dp)
  ) {
    for (row in 0..14) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(1.dp)
      ) {
        for (col in 0..14) {
          Box(
            modifier = Modifier
              .weight(1f)
              .aspectRatio(1f)
              .background(Color.LightGray),
            contentAlignment = Alignment.Center,
          ) {
            boardState.board[row, col].multiplier?.string?.let {
              Text(it)
            }
          }
        }
      }
    }
  }
}

@Composable
fun rememberBoardState(board: Board): BoardState {
  return remember(board) { BoardState(board) }
}

class BoardState(val board: Board)


@Preview
@Composable
private fun PreviewBoard() {
  val bag = remember { BagOfTiles() }
  Box(Modifier.fillMaxSize()) {
    Board(
      boardState = rememberBoardState(
        board = remember {
          Board {
            repeat(5) {
              bag.pickRandomTile()?.placeAt(
                Board.Center.next(Direction.Horizontal, count = it)
              )
            }
          }
        },
      ),
    )
  }
}
