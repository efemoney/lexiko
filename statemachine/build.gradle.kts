plugins {
  kotlin("jvm")
}

dependencies {
  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinx.coroutines.core)
}

dependencies {
  testImplementation(Deps.junit)
  testImplementation(Deps.turbine)
  testImplementation(Deps.kotlin.test)
  testImplementation(Deps.kotlinx.datetime)
  testImplementation(Deps.kotlinx.coroutines.test)
}
