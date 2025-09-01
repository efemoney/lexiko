plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.poko)
}

kotlin {
  android {}
  jvm()

  sourceSets {
    commonMain.dependencies {
      implementation(projects.libsUtil)
      implementation(projects.presentationApi)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.jetbrains.compose.runtime)
      implementation(libs.circuit.runtime)
      implementation(libs.circuit.foundation)
      implementation(libs.circuit.backstack)
      implementation(libs.circuitx.overlays)
      implementation(libs.molecule.runtime)
    }
    androidMain.dependencies {
      implementation(libs.circuitx.android)
      implementation(libs.circuitx.gestureNavigation)
    }
    jvmMain.dependencies {
      implementation(compose.desktop.common)
      implementation(compose.desktop.currentOs)
    }
  }
}
