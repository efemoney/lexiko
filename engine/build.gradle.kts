plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  kotlinCompilerPluginClasspath(libs.androidx.compose.compiler)

  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.immutable)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.jetbrains.compose.runtime)
  implementation(libs.molecule.runtime)

  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.junit)
}
