package dev.efemoney.lexiko.app.internal

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import coil.ImageLoader
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import dev.efemoney.lexiko.CoreComponent
import dev.efemoney.lexiko.app.LexikoApplication
import dev.efemoney.lexiko.app.navigation.RealNavigator
import dev.efemoney.lexiko.lobby.LobbyPresenter
import javax.inject.Scope
import javax.inject.Singleton

@Scope
internal annotation class Retained

@Singleton
@Component(
  modules = [SingletonModule::class],
  dependencies = [CoreComponent::class]
)
internal interface SingletonComponent {

  val retainedComponentFactory: ForegroundComponent.Factory

  @Component.Factory
  interface Factory {

    fun create(
      @BindsInstance context: Context,
      core: CoreComponent
    ): SingletonComponent
  }
}

@Retained
@Subcomponent(modules = [ForegroundModule::class])
internal abstract class ForegroundComponent : ViewModel() {

  abstract val imageLoader: ImageLoader

  abstract val navigator: RealNavigator

  abstract val lobbyPresenter: LobbyPresenter

  @Subcomponent.Factory
  abstract class Factory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = create() as T

    abstract fun create(): ForegroundComponent
  }
}

@Composable
internal fun component() = RetainedComponent(LocalViewModelStoreOwner.current!!, LocalContext.current)

private fun RetainedComponent(owner: ViewModelStoreOwner, context: Context): ForegroundComponent =
  ViewModelProvider(owner, (context.applicationContext as LexikoApplication).component.retainedComponentFactory).get()
