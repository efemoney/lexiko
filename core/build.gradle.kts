plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
}

android.namespace = "dev.efemoney.lexiko"

dependencies {
  implementation(projects.engine)

  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinx.coroutines.core)
  implementation(Deps.kotlinx.coroutines.android)

  implementation(Deps.dagger)
  kapt(Deps.dagger.compiler)

  api(Deps.ktor.client)
  api(Deps.ktor.client.okHttp)

  implementation(Deps.okio)
  implementation(Deps.okHttp)
  implementation(Deps.okHttp.logging)

  implementation(Deps.uuid)

  implementation(Deps.androidx.navigation.runtime)
}

dependencies {
  testImplementation(Deps.kotlin.test)
  testImplementation(Deps.kotlinx.coroutines.test)

  testImplementation(Deps.junit)
}
