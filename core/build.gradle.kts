plugins {
  plugin("android-library")

  kotlin("multiplatform")
  plugin("target-android")
  plugin("target-ios")

  kotlin("kapt")
}

android.namespace = "dev.efemoney.lexiko"

dependencies {
  commonMainImplementation(project(":engine"))

  commonMainImplementation(Deps.kotlin.stdlib.common)
  androidMainImplementation(Deps.kotlin.stdlib.jdk8)

  // Coroutines
  commonMainImplementation(Deps.kotlinx.coroutines.core)
  androidMainImplementation(Deps.kotlinx.coroutines.android)

  // DI
  androidMainImplementation(Deps.dagger)
  kapt(Deps.dagger.compiler)

  // IO (Disk + Networking)
  commonMainApi(Deps.ktor.client)
  commonMainImplementation(Deps.okio.multiplatform)

  androidMainApi(Deps.ktor.client.okHttp)
  androidMainImplementation(Deps.okHttp)
  androidMainImplementation(Deps.okHttp.logging)

  // UUID
  commonMainImplementation(Deps.uuid)

  // Navigation
  androidMainImplementation(Deps.androidx.navigation.runtime)
}

dependencies {
  commonTestImplementation(kotlin("test"))

  androidTestImplementation(Deps.junit)
  androidTestImplementation(Deps.kotlinx.coroutines.test)
}
