plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

android.buildFeatures.compose = true

dependencies {
  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinx.immutable.jvm)
  implementation(Deps.kotlinx.coroutines.android)

  implementation(projects.core)

  implementation(Deps.androidx.core)
  implementation(Deps.androidx.activity)
  implementation(Deps.androidx.activity.compose)
  implementation(Deps.androidx.appcompat)
  implementation(Deps.androidx.fragment)
  implementation(Deps.androidx.navigation.compose)

  implementation(Deps.androidx.compose.runtime)
  implementation(Deps.androidx.compose.ui)
  implementation(Deps.androidx.compose.ui.tooling)
  implementation(Deps.androidx.compose.material)
  // implementation(Deps.androidx.compose.material3)
  implementation(Deps.androidx.compose.material.icons)
  implementation(Deps.androidx.compose.material.moarIcons)

  implementation(Deps.coil)
  implementation(Deps.coil.compose)
  implementation(Deps.accompanist.insets)
  implementation(Deps.accompanist.insetsUi)
  implementation(Deps.accompanist.systemUiController)

  implementation(Deps.dagger)
  kapt(Deps.dagger.compiler)
}
