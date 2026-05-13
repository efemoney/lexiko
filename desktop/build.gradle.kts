plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.metro)
}

compose {
  desktop {
    application {
      mainClass = "dev.efemoney.lexiko.app.LexikoAppKt"
    }
  }
}

metro {
  debug = true
}

dependencies {
  implementation(projects.libsUtil)
  implementation(projects.uiCommon)
  implementation(projects.app)

  implementation(libs.jetbrains.compose.desktop)
  implementation(libs.jetbrains.compose.desktop.currentOs)
}
