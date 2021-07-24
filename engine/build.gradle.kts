plugins {
  kotlin("multiplatform")
  plugin("target-android")
  plugin("target-ios")
  plugin("android-library")
}

android.namespace = "dev.efemoney.lexiko.engine"

dependencies {
  commonMainImplementation(Deps.kotlin.stdlib.common)
  commonMainImplementation(Deps.kotlinx.coroutines.core)
  commonMainImplementation(projects.statemachine)
}

dependencies {
  commonTestImplementation(Deps.kotlin.test)

  androidTestImplementation(Deps.junit)
  androidTestImplementation(Deps.kotlinx.coroutines.test)
}
