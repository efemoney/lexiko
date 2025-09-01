plugins {
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
}

kotlin {
  android { namespace = "dev.efemoney.lexiko.ui" }
  jvm()

  sourceSets {
    commonMain.dependencies {
      api(libs.jetbrains.compose.runtime)
      api(libs.jetbrains.compose.foundation)
      api(libs.jetbrains.compose.ui)
      api(libs.coil.compose)
    }
  }
}
