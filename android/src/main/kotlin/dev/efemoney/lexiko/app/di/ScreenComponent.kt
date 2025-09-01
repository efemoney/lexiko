@file:Suppress("UNCHECKED_CAST")

package dev.efemoney.lexiko.app.di

import com.slack.circuit.overlay.OverlayHost
import dev.efemoney.lexiko.di.ScreenScope
import dev.efemoney.lexiko.lobby.di.LobbyContributor
import dev.efemoney.lexiko.presentation.Navigator
import dev.efemoney.lexiko.presentation.Presenter
import dev.efemoney.lexiko.presentation.Screen
import dev.efemoney.lexiko.presentation.Ui
import dev.efemoney.lexiko.presentation.UiState
import dev.efemoney.lexiko.presentation.circuit.di.PresenterImplementation
import dev.efemoney.lexiko.presentation.circuit.di.ScreenComponent
import dev.efemoney.lexiko.presentation.circuit.di.UiImplementation
import dev.efemoney.lexiko.presentation.di.Contribution
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides
import kotlin.collections.associate

@Component
@ScreenScope
internal abstract class ScreenComponentImpl(
  @Component protected val app: AppComponent
) : ScreenComponent {

  override fun presenter(navigator: Navigator, overlays: OverlayHost): PresenterImplementation =
    /*PresenterComponentImpl.create(this, navigator, overlays).presenterImplementation*/ TODO()

  override fun ui(): UiImplementation =
    /*UiComponentImpl.create(this).uiImplementation*/ TODO()

  companion object
}

@Component
internal abstract class PresenterComponentImpl(
  @Component protected val screen: ScreenComponent,
  @get:Provides protected val navigator: Navigator,
  @get:Provides protected val overlays: OverlayHost,
) : LobbyContributor {
  companion object;
  abstract val contributions: Set<Contribution<*, *>>
  abstract val implementation: RealPresenterImplementation
}

@Component
internal abstract class UiComponentImpl(
  @Component protected val screen: ScreenComponent,
) : LobbyContributor {
  companion object;
  abstract val contributions: Set<Contribution<*, *>>
  abstract val implementation: RealUiImplementation
}

@Inject
@ScreenScope
internal class RealPresenterImplementation(set: Set<Contribution<*, *>>) : PresenterImplementation {
  private val map = set.associate { it.screen to it.presenter }
  override fun <Args : Screen, State : UiState> of(screen: Args) = map[screen::class]!!() as Presenter<Args, State>
}

@Inject
@ScreenScope
internal class RealUiImplementation(set: Set<Contribution<*, *>>) : UiImplementation {
  private val map = set.associate { it.screen to it.ui }
  override fun <Args : Screen, State : UiState> of(screen: Args) = map[screen::class]!!() as Ui<State>
}
