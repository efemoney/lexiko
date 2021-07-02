plugins {
  plugin("android-library")

  kotlin("multiplatform")
  plugin("target-android")
  plugin("target-ios")
}

android.namespace = "dev.efemoney.lexiko.engine"

dependencies {
  commonMainImplementation(Deps.kotlin.stdlib.common)
  commonMainImplementation(Deps.kotlinx.coroutines.core)
}

dependencies {
  commonTestImplementation(kotlin("test"))

  androidTestImplementation("junit:junit:4.13.2")
  androidTestImplementation(Deps.kotlinx.coroutines.test)
}
