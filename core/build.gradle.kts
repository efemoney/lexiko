plugins {
  id("plugin-multiplatform")
  id("plugin-android-target")
  id("plugin-ios-target")
  kotlin("kapt")
}

dependencies {
  commonMainImplementation(project(":engine"))

  commonMainImplementation(Deps.kotlin.stdlib.common)
  androidMainImplementation(Deps.kotlin.stdlib.jdk8)

  commonMainImplementation(Deps.kotlinx.coroutines.core)
  androidMainImplementation(Deps.kotlinx.coroutines.android)

  // DI
  androidMainImplementation(Deps.dagger)
  kapt(Deps.dagger.compiler)

  // IO (Disk + Networking)
  commonMainImplementation(Deps.okio.multiplatform)
  commonMainImplementation(Deps.ktor.client.core)

  androidMainImplementation(Deps.okio)
  androidMainImplementation(Deps.okHttp)
  androidMainImplementation(Deps.okHttp.logging)
  androidMainImplementation(Deps.ktor.client.okHttp)
}
