plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
}

kotlin {
  android { namespace = "dev.efemoney.lexiko.app" }
  jvm()

  sourceSets {
    commonMain.dependencies {
      implementation(projects.libsUtil)
      implementation(projects.presentationApi)
      implementation(projects.presentationNav3)
      implementation(projects.uiCommon)
      implementation(projects.uiLobby)
    }
  }
}
