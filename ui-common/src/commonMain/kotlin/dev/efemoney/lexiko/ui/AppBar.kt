package dev.efemoney.lexiko.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import dev.drewhamilton.poko.Poko

@Poko
@Immutable
class AppBarState(
  val title: String,
)

@Composable
fun AppBar(
  state: AppBarState,
  modifier: Modifier = Modifier,
) {
}
