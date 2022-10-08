plugins {
  application
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.noarg)
  alias(libs.plugins.ksp)
}

application {
  mainClass = "io.ktor.server.netty.EngineMain"
}

noArg {
  annotation("dev.efemoney.lexiko.NoArgConstructor")
}

dependencies {
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.okio)
  implementation(libs.okHttp)
  implementation(libs.okHttp.logging)

  implementation(libs.moshi)
  implementation(libs.moshi.lazyAdapters)
  ksp(libs.moshi.codegen)

  implementation(libs.ktor.server)
  implementation(libs.ktor.server.netty)
  implementation(libs.ktor.server.plugins.auth)
  implementation(libs.ktor.server.plugins.autoHeadResponse)
  implementation(libs.ktor.server.plugins.cors)
  implementation(libs.ktor.server.plugins.compression)
  implementation(libs.ktor.server.plugins.conNeg)
  implementation(libs.ktor.server.plugins.callLogging)
  implementation(libs.ktor.server.plugins.websockets)
  implementation(libs.ktor.server.plugins.auth)
  implementation(libs.ktor.server.moshi)
  implementation(libs.ktor.websockets)

  implementation(libs.dagger)
  ksp(libs.dagger.compiler)

  implementation(libs.firebase.admin)
}
