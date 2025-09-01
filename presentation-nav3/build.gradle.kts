plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.compose.multiplatform)
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
      implementation(projects.presentationApi)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.jetbrains.compose.runtime)
      implementation(libs.jetbrains.compose.foundation)
      implementation(libs.jetbrains.compose.ui)
      implementation(libs.androidx.nav3.runtime)
      implementation(libs.androidx.nav3.ui)
      implementation(libs.molecule.runtime)
      implementation(libs.kmpUri)
    }
    androidMain.dependencies {
      implementation(libs.androidx.lifecycle.viewmodel.nav3)
    }
    jvmMain.dependencies {
      implementation(compose.desktop.common)
      implementation(compose.desktop.currentOs)
    }
  }
}
