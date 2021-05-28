plugins {
  id("plugin-simple-layout")
  application
  kotlin
  `kotlin-kapt`
  `kotlin-noarg`
  com.google.devtools.ksp
}

sourceSets.configureEach {
  java.srcDirs(
    "build/generated/ksp/$name",
    "build/generated/source/kapt/$name"
  )
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
  ksp(Deps.moshix.ksp)

  implementation(Deps.ktor.server)
  implementation(Deps.ktor.server.netty)
  implementation(Deps.ktor.server.moshi)
  implementation(Deps.ktor.auth)
  implementation(Deps.ktor.websockets)

  implementation(Deps.dagger)
  kapt(Deps.dagger.compiler)

  implementation("com.google.firebase:firebase-admin:7.3.0")
}
