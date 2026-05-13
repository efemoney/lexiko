package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import dev.efemoney.lexiko.presentation.UiState
import dev.efemoney.lexiko.util.RetainObserverAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@PublishedApi
internal class Presentation<T : UiState>(produceT: @Composable () -> T) : RetainObserverAdapter() {
  val scope = CoroutineScope(SupervisorJob())
  val state by scope.moleculeState(body = produceT)
  override fun onRetired() = scope.cancel()
  override fun onUnused() = scope.cancel()
}

private fun <T> CoroutineScope.moleculeState(
  mode: RecompositionMode = RecompositionMode.Immediate,
  body: @Composable () -> T,
): State<T> {
  var state: MutableState<T>? = null
  launchMolecule(
    mode = mode,
    emitter = {
      val outputState = state
      if (outputState != null) outputState.value = it else state = mutableStateOf(it)
    },
    body = body,
  )
  return state!!
}
