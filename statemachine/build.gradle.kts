plugins {
  kotlin("jvm")
  plugin("simple-layout")
}

dependencies {
  implementation(Deps.kotlin.stdlib.common)
  implementation(Deps.kotlinx.coroutines.core)
}

dependencies {
  testImplementation(Deps.junit)
  testImplementation(Deps.kotlin.test)
  testImplementation(Deps.kotlinx.datetime)
  testImplementation(Deps.kotlinx.coroutines.test)
}
