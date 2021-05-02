package dev.efemoney.lexiko

import dagger.Component
import dev.efemoney.lexiko.engine.api.Games
import dev.efemoney.lexiko.engine.api.Players
import dev.efemoney.lexiko.internal.CoreModule
import io.ktor.client.*
import okhttp3.OkHttpClient

@Component(modules = [CoreModule::class])
actual interface CoreComponent {

  val okHttpClient: OkHttpClient

  actual val httpClient: HttpClient

  actual val games: Games

  actual val players: Players

  @Component.Factory
  interface Factory {

    fun create(): CoreComponent
  }
}
