plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.metro)
}

kotlin {
  android { namespace = "dev.efemoney.lexiko.util" }
  jvm()

  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.jetbrains.compose.runtime)
    }
    androidMain.dependencies {
      implementation(libs.kotlinx.coroutines.android)
    }
    jvmMain.dependencies {
      implementation(libs.kotlinx.coroutines.swing)
    }
  }
}
