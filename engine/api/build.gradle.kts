plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.immutable)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.jetbrains.compose.runtime)
  implementation(libs.jetbrains.compose.ui.util)
}
