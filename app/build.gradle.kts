plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.ksp)
}

android {
  namespace = "dev.efemoney.lexiko.app"
  defaultConfig {
    targetSdk = 34
  }
}

dependencies {
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.immutable.jvm)
  implementation(libs.kotlinx.coroutines.android)

  implementation(libs.androidx.core)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.fragment)
  implementation(libs.androidx.navigation.compose)

  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material.icons)
  implementation(libs.androidx.compose.material.icons.extended)

  implementation(libs.coil)
  implementation(libs.coil.okhttp)
  implementation(libs.coil.compose)

  implementation(libs.dagger)
  ksp(libs.dagger.compiler)
}
