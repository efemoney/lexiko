package dev.efemoney.lexiko

import dagger.Component
import dev.efemoney.lexiko.internal.LexikoModule
import io.ktor.client.*

@Component(modules = [LexikoModule::class])
actual interface LexikoComponent {

  actual val httpClient: HttpClient

  @Component.Factory
  interface Factory {

    fun create(): LexikoComponent
  }
}
