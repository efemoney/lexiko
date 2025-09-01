plugins {
  alias(libs.plugins.kotlin.multiplatform)
//  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
  alias(libs.plugins.poko)
}

kotlin {
//  android {}
  jvm()
//  iosArm64()

  sourceSets {
    commonMain.dependencies {
      implementation(projects.engineApi)
      implementation(libs.androidx.collection)
      implementation(libs.kotlinx.coroutines.core)
    }
    /*androidMain.dependencies {
      implementation(libs.kotlinx.coroutines.android)
    }*/
  }
}
