package dev.efemoney.lexiko.internal

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient

@Module
internal interface CoreModule {

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
