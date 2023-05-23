plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ksp)
}

dependencies {
  kotlinCompilerPluginClasspath(libs.androidx.compose.compiler)
  ksp(libs.kotlinInject.compiler)

  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.immutable)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.jetbrains.compose.runtime)
  implementation(libs.jetbrains.compose.ui.util)
  implementation(libs.molecule.runtime)
  implementation(libs.kotlinInject.runtime)

  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.turbine)
  testImplementation(libs.junit)
}
