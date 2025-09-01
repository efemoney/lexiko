plugins {
  alias(libs.plugins.kotlin.multiplatform)
//  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.poko)
}

kotlin {
//  android {}
  jvm()
//  iosArm64()
//  js { browser() }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.jetbrains.compose.runtime)
      implementation(libs.jetbrains.compose.ui.util)
      implementation(libs.androidx.collection)
    }
  }
}
