plugins {
  kotlin("jvm")
}

dependencies {
  implementation(Deps.kotlin.stdlib.jdk8)
  testImplementation(Deps.kotlin.test)
}

tasks.test {
  useJUnitPlatform()
  testLogging { setExceptionFormat("full") }
}
