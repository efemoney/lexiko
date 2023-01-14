plugins {
  kotlin("jvm")
}

dependencies {
  implementation(projects.statemachine)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines.core)
}

dependencies {
  implementation(libs.kotlin.test)
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.junit)
}
