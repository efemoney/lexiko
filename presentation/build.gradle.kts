plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.metro)
  alias(libs.plugins.poko)
}

kotlin {
  android {}
  jvm()
  sourceSets {
    commonMain.dependencies {
      implementation(projects.libsUtil)
      implementation(projects.libsDi)
      implementation(libs.androidx.compose.runtime)
      implementation(libs.jetbrains.compose.foundation)
      implementation(libs.jetbrains.compose.ui)
      implementation(libs.androidx.nav3.runtime)
      implementation(libs.jetbrains.nav3.ui)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.molecule.runtime)
      implementation(libs.uriKmp)
    }
    androidMain.dependencies {
      implementation(libs.androidx.compose.foundation)
      implementation(libs.androidx.compose.ui)
      implementation(libs.androidx.nav3.ui)
    }
  }
}
