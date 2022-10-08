plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.jetbrains.compose.runtime)

  testImplementation(libs.turbine)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.datetime)
  testImplementation(libs.kotlinx.coroutines.test)
}

tasks.test {
  useJUnitPlatform()
  testLogging { setExceptionFormat("full") }
}
