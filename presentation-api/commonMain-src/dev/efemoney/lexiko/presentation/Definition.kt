package dev.efemoney.lexiko.presentation

import dev.drewhamilton.poko.Poko

@Poko
class ScreenDefinition<Args : Screen, State : UiState>(
  val presenter: () -> Presenter<Args, State>,
  val ui: () -> Ui<State>,
)
