plugins {
  id("plugin-android-app")
  id("plugin-simple-layout")
  id("plugin-compose-support")
}

dependencies {
  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinx.immutable.jvm)
  implementation(Deps.kotlinx.coroutines.android)

  implementation(project(":core"))

  implementation(Deps.androidx.core)
  implementation(Deps.androidx.activity)
  implementation(Deps.androidx.appcompat)
  implementation(Deps.androidx.fragment)

  implementation(Deps.androidx.compose.runtime)
  implementation(Deps.androidx.compose.ui)
  implementation(Deps.androidx.compose.ui.tooling)
  implementation(Deps.androidx.compose.material)
}
