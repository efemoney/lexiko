package dev.efemoney.lexiko.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey

@Stable
interface Screen : NavKey

interface Presenter<in Args : Screen, out State : UiState> {

  @Composable
  fun run(screen: Args): State
}


@Stable
interface UiState

interface Ui<in State : UiState> {

  @Composable
  fun Content(state: State, modifier: Modifier = Modifier)
}
