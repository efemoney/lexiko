package dev.efemoney.lexiko.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
interface Screen

@Stable
interface UiState

interface Presenter<in Args : Screen, out State : UiState> {

  @Composable
  fun run(screen: Args): State
}

interface Ui<in State : UiState> {

  @Composable
  fun Content(state: State, modifier: Modifier = Modifier)
}
