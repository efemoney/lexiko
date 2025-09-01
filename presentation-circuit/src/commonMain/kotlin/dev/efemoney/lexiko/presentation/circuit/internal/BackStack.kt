@file:OptIn(ExperimentalUuidApi::class)

package dev.efemoney.lexiko.presentation.circuit.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.Snapshot.Companion.withoutReadObservation
import com.slack.circuit.backstack.BackStack
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.screen.PopResult
import dev.drewhamilton.poko.Poko
import dev.efemoney.lexiko.presentation.Navigator
import dev.efemoney.lexiko.presentation.Result
import dev.efemoney.lexiko.presentation.ReturnsResult
import dev.efemoney.lexiko.presentation.Screen
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.slack.circuit.runtime.Navigator as CircuitNavigator
import com.slack.circuit.runtime.screen.Screen as CircuitScreen

@Composable
internal fun rememberOurResults(): OurResults = rememberRetained { OurResults() }

@Composable
internal fun rememberOurBackStack(root: Screen): OurBackStack {
  val backStack = rememberSaveable(saver = OurBackStack.Saver) { OurBackStack(root) }
  backStack.results = rememberOurResults()

  DisposableEffect(backStack) {
    onDispose {
      backStack.results
    }
  }

  return backStack
}

internal class OurBackStack private constructor(initialRoot: OurRecord? = null) : BackStack<OurRecord> {

  constructor(root: Screen) : this(OurRecord(root))

  lateinit var results: OurResults

  private val entries = mutableStateListOf<OurRecord>().also { if (initialRoot != null) it.add(initialRoot) }

  override val size get() = entries.size

  override val topRecord get() = entries.firstOrNull()

  override val rootRecord get() = entries.lastOrNull()

  override fun iterator() = entries.iterator()

  override fun push(screen: CircuitScreen, resultKey: String?): Boolean {
    return screen is Screen && push(OurRecord(screen), resultKey)
  }

  override fun push(record: OurRecord, resultKey: String?): Boolean {
    val current = withoutReadObservation { entries.firstOrNull() }
    if (current?.screen == record.screen) return false
    entries.add(0, record)
    return true
  }

  override fun pop(result: PopResult?): OurRecord? {
    val current = entries.removeFirstOrNull()
    val prev = entries.firstOrNull() ?: return current
    when (result) {
      null, !is Result -> Unit
      else -> {
        results.sendResult(prev, result)
      }
    }
    return current
  }

  override fun containsRecord(record: OurRecord, includeSaved: Boolean) = record in entries

  override fun isRecordReachable(key: String, depth: Int, includeSaved: Boolean): Boolean {
    TODO("Not yet implemented")
  }

  override fun saveState() = TODO("Not implemented")

  override fun restoreState(screen: CircuitScreen) = TODO("Not implemented")

  companion object {
    val Saver = listSaver(
      save = { value ->
        value.entries.mapNotNull {
          with(OurRecord.Saver) { save(it) }
        }
      },
      restore = { value ->
        OurBackStack().also { backStack ->
          value.mapNotNullTo(backStack.entries) {
            with(OurRecord.Saver) { restore(it) }
          }
        }
      },
    )
  }
}

@Poko
internal class OurRecord(
  override val screen: Screen,
  override val key: String = Uuid.random().toString(),
) : BackStack.Record {

  override suspend fun awaitResult(key: String) = TODO()

  companion object {
    val Saver = mapSaver(
      save = { value ->
        buildMap {
          put("screen", value.screen)
          put("key", value.key)
        }
      },
      restore = { map ->
        OurRecord(
          screen = map["screen"] as Screen,
          key = map["key"] as String,
        )
      },
    )
  }
}

internal class OurNavigator(
  private val backStack: OurBackStack,
  private val navigator: CircuitNavigator,
) : Navigator {

  override fun navigate(to: Screen) {
    navigator.goTo(to)
  }

  override fun <R : Result> pop(result: R) {
    navigator.pop(result)
  }

  override fun popUntil(predicate: (Screen) -> Boolean) {
    navigator.popUntil { it is Screen && predicate(it) }
  }

  override suspend fun <S, R : Result> goToForResult(screen: S): R? where S : Screen, S : ReturnsResult<R> {
    val current = requireNotNull(backStack.topRecord)
    backStack.push(screen)
    @Suppress("UNCHECKED_CAST")
    return backStack.results.readResult(current) as? R
  }
}

@Stable
internal class OurResults {

  private val channels = mutableMapOf<String, Channel<Result>>()

  fun isAwaitingResult(to: OurRecord): Boolean {
    return to.channel.isEmpty
  }

  fun sendResult(to: OurRecord, result: Result) {
    to.channel.trySend(result)
  }

  suspend fun readResult(record: OurRecord): Result {
    return record.channel.receive()
  }

  fun tryReadResult(record: OurRecord): Result? {
    return record.channel.tryReceive().getOrNull()
  }

  private inline val OurRecord.channel
    get() = channels.getOrPut(key) { Channel(1, BufferOverflow.DROP_OLDEST) }
}
