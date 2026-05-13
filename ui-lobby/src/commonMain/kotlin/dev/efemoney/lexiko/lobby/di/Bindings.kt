package dev.efemoney.lexiko.lobby.di

import dev.efemoney.lexiko.di.ForegroundScope
import dev.efemoney.lexiko.lobby.LobbyPresenter
import dev.efemoney.lexiko.lobby.LobbyUi
import dev.efemoney.lexiko.presentation.nav3.NavEntryProvider
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides

@ContributesTo(ForegroundScope::class)
internal interface LobbyBindings {

  @Provides
  @IntoSet
  fun lobbyScreen(
    presenter: Provider<LobbyPresenter>,
    ui: Provider<LobbyUi>,
  ): NavEntryProvider = NavEntryProvider {
    screenEntry(
      presenter = presenter,
      ui = ui,
    )
  }
}
