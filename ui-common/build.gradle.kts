plugins {
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.poko)
}

kotlin {
  android { namespace = "dev.efemoney.lexiko.ui" }
  jvm()

  sourceSets {
    commonMain.dependencies {
      api(libs.androidx.compose.runtime)
      api(libs.jetbrains.compose.foundation)
      api(libs.jetbrains.compose.ui)
      api(libs.jetbrains.compose.material3)
      api(libs.coil.compose)
    }
  }
}
