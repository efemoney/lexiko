plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.android.multiplatform)
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
      implementation(projects.engineApi)
      implementation(projects.presentationApi)
      implementation(projects.uiCommon)
      implementation(libs.coil.compose)
    }
  }
}
