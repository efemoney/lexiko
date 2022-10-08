plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp)
}

android.namespace = "dev.efemoney.lexiko"

dependencies {
  implementation(projects.engine)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.dagger)
  ksp(libs.dagger.compiler)
  api(libs.ktor.client)
  api(libs.ktor.client.okHttp)
  implementation(libs.okio)
  implementation(libs.okHttp)
  implementation(libs.okHttp.logging)
  implementation(libs.uuid)
  implementation(libs.androidx.navigation.runtime)

  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.junit)
}
