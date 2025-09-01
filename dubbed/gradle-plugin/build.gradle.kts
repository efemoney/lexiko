plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  implementation(libs.kotlin.stdlib)
  testImplementation(libs.kotlin.test)
}

tasks.test {
  useJUnitPlatform()
  testLogging { setExceptionFormat("full") }
}
