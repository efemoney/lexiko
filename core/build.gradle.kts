plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
}

android.namespace = "dev.efemoney.lexiko"

dependencies {
  implementation(projects.engine)

  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.coroutines.android)

  implementation(libs.dagger)
  kapt(libs.dagger.compiler)

  api(libs.ktor.client)
  api(libs.ktor.client.okHttp)

  implementation(libs.okio)
  implementation(libs.okHttp)
  implementation(libs.okHttp.logging)

  implementation(libs.uuid)

  implementation(libs.androidx.navigation.runtime)
}

dependencies {
  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.coroutines.test)

  testImplementation(libs.junit)
}
