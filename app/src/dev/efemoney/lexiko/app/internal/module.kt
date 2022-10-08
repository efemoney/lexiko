package dev.efemoney.lexiko.app.internal

import android.content.Context
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.efemoney.lexiko.app.navigation.RealNavigator
import dev.efemoney.lexiko.internal.Dispatchers
import dev.efemoney.lexiko.internal.ForegroundScope
import dev.efemoney.lexiko.navigation.Navigator
import okhttp3.OkHttpClient

@Module
internal interface SingletonModule {
  companion object {

    @Provides
    @Reusable
    fun imageLoader(context: Context, okHttp: dagger.Lazy<OkHttpClient>) =
      ImageLoader.Builder(context)
        .callFactory { okHttp.get() }
        .components {}
        .build()
  }
}

@Module
internal interface ForegroundModule {

  @Binds
  fun RealNavigator.asNavigator(): Navigator

  @Binds
  fun RealDispatchers.asDispatchers(): Dispatchers

  @Binds
  fun RealForegroundScope.asForegroundScope(): ForegroundScope
}
