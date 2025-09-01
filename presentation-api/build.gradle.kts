plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.metro)
  alias(libs.plugins.poko)
}

kotlin {
  android { namespace = "dev.efemoney.lexiko.presentation" }
  jvm()

  sourceSets {
    commonMain.dependencies {
      implementation(projects.libsUtil)
      implementation(projects.libsDi)
      api(libs.jetbrains.compose.runtime)
      api(libs.jetbrains.compose.ui)
      api(libs.androidx.nav3.runtime)
      api(libs.androidx.nav3.ui)
      api(libs.kmpUri)
    }
  }
}
