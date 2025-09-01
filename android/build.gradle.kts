plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
}

android {
  namespace = "dev.efemoney.lexiko.app"
}

metro {
  debug = true
}

dependencies {
  implementation(projects.libsUtil)
  implementation(projects.presentationApi)
  implementation(projects.presentationNav3)
  implementation(projects.uiCommon)
  implementation(projects.uiLobby)
  implementation(projects.app)

  implementation(libs.androidx.core)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity.compose)
  implementation(libs.coil.compose)
  implementation(libs.coil.ktor)
}
