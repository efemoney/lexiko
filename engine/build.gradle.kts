plugins {
  id("com.android.library")
  kotlin("android")
}

android.namespace = "dev.efemoney.lexiko.engine"

dependencies {
  implementation(projects.statemachine)

  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinx.coroutines.core)
}

dependencies {
  implementation(Deps.kotlin.test)
  implementation(Deps.kotlinx.coroutines.test)
  implementation(Deps.junit)
}
