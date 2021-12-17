package dev.efemoney.lexiko

import dagger.Component
import dev.efemoney.lexiko.internal.CoreModule
import io.ktor.client.*
import okhttp3.OkHttpClient

@Component(modules = [CoreModule::class])
actual interface CoreComponent {

  val okHttpClient: OkHttpClient

  actual val httpClient: HttpClient

  @Component.Factory
  interface Factory {

    fun create(): CoreComponent
  }
}
