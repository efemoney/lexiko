plugins {
  application
  kotlin("jvm")
  kotlin("kapt")
  kotlin("plugin.noarg")
}

application.mainClass.set("io.ktor.server.netty.EngineMain")

noArg.annotation("dev.efemoney.lexiko.NoArgConstructor")

dependencies {
  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinx.coroutines.core)

  implementation(Deps.okio)
  implementation(Deps.okHttp)
  implementation(Deps.okHttp.logging)

  implementation(Deps.moshi)
  implementation(Deps.moshi.lazyAdapters)
  kapt(Deps.moshi.codegen)

  implementation(Deps.ktor.server)
  implementation(Deps.ktor.server.netty)
  implementation(Deps.ktor.server.plugins.auth)
  implementation(Deps.ktor.server.plugins.autoHeadResponse)
  implementation(Deps.ktor.server.plugins.cors)
  implementation(Deps.ktor.server.plugins.compression)
  implementation(Deps.ktor.server.plugins.conNeg)
  implementation(Deps.ktor.server.plugins.callLogging)
  implementation(Deps.ktor.server.plugins.websockets)
  implementation(Deps.ktor.server.plugins.auth)
  implementation(Deps.ktor.server.moshi)
  implementation(Deps.ktor.websockets)

  implementation(Deps.dagger)
  kapt(Deps.dagger.compiler)

  implementation(Deps.firebase.admin)
}
