package dev.efemoney.lexiko.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.efemoney.lexiko.app.internal.component
import dev.efemoney.lexiko.app.lobby.LobbyScreen
import dev.efemoney.lexiko.navigation.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun LexikoApp() {
  LexikoTheme {
    LexikoAppContent()
  }
}

@Composable
fun LexikoAppContent() {
  val navEvents = component().navigator.directions.receiveAsFlow()
  val navController = rememberNavController()

  LaunchedEffect(navController) {
    navEvents.onEach {
      if (it !is CustomDirection) navController.navigate(it)
      else when (it) {
        is DeepLink -> navController.navigate(it.deepLink.toUri())
        is ComposeScreen -> navController.navigate(it.screenName)
        is PopBackStack -> {
          if (it.popUpTo == NO_ID) navController.popBackStack()
          else navController.popBackStack(it.popUpTo, it.popUpToInclusive)
        }
      }
    }.collect()
  }

  NavHost(navController, startDestination = lobby) {
    composable(lobby) { LobbyScreen() }
    composable(profile) { }
    composable(game) { }
  }
}

private const val game = "game"
private const val lobby = "lobby"
private const val profile = "profile"
