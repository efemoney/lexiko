plugins {
  kotlin("jvm")
}

dependencies {
  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinx.coroutines.core)

  testImplementation(Deps.turbine)
  testImplementation(Deps.kotlin.test)
  testImplementation(Deps.kotlinx.datetime)
  testImplementation(Deps.kotlinx.coroutines.test)
}

tasks.test {
  useJUnitPlatform()
  testLogging { setExceptionFormat("full") }
}
