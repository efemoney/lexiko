package dev.efemoney.lexiko.internal

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.efemoney.lexiko.engine.api.Games
import dev.efemoney.lexiko.engine.api.Players
import dev.efemoney.lexiko.local.LocalGames
import dev.efemoney.lexiko.local.LocalPlayers
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient

@Module
internal interface CoreModule {

  @Binds
  fun LocalGames.asGames(): Games

  @Binds
  fun LocalPlayers.asPlayers(): Players

  companion object {

    @Provides
    @Reusable
    fun okHttpClient() = OkHttpClient.Builder().build()

    @Provides
    @Reusable
    fun httpClient(okHttpClient: OkHttpClient) = HttpClient(OkHttp) {
      engine { preconfigured = okHttpClient }
    }
  }
}
