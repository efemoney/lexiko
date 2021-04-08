package dev.efemoney.lexiko.internal

import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.efemoney.lexiko.engine.Games
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
internal interface LexikoModule {

  @Binds
  fun games(impl: Games): Games

  companion object {

    @Provides
    fun httpClient(authInterceptor: AuthInterceptor) = HttpClient(OkHttp) {
      engine {
        clientCacheSize = 0
        addInterceptor(authInterceptor)
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
      }
    }
  }
}
