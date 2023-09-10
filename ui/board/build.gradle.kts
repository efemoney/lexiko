plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.anvil)
}

android {
  namespace = "dev.efemoney.lexiko.ui.board"
  buildFeatures.compose = true
}

dependencies {
  implementation(projects.engine)
  implementation(libs.bundles.androidx.compose.core.ui)
  implementation(libs.androidx.compose.ui.tooling.preview)
  if (providers.systemProperty("CI").orNull != "true")
    implementation(libs.androidx.compose.ui.tooling)
}
