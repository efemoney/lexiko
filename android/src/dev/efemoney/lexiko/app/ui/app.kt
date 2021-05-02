package dev.efemoney.lexiko.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.efemoney.lexiko.app.internal.retainedComponent
import dev.efemoney.lexiko.app.lobby.LobbyScreen
import dev.efemoney.lexiko.navigation.CustomDirection
import dev.efemoney.lexiko.navigation.NO_ID
import dev.efemoney.lexiko.navigation.PopBackStack
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun LexikoApp() {
  val navController = rememberNavController()

  val navEvents = retainedComponent()
    .realNavigator
    .directions
    .receiveAsFlow()

  LaunchedEffect(navController) {
    navEvents.collect {
      if (it !is CustomDirection) navController.navigate(it) else when (it) {
        is PopBackStack -> if (it.popUpTo == NO_ID) {
          navController.popBackStack()
        } else {
          navController.popBackStack(it.popUpTo, it.popUpToInclusive)
        }
      }
    }
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
