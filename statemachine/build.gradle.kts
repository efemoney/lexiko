plugins {
  kotlin("multiplatform")
  plugin("target-android")
  plugin("target-ios")
  plugin("target-jvm")
  plugin("android-library")
}

android.namespace = "dev.efemoney.lexiko.statemachine"

dependencies {
  commonMainImplementation(Deps.kotlin.stdlib.common)
  commonMainImplementation(Deps.kotlinx.coroutines.core)
}

dependencies {
  commonTestImplementation(Deps.kotlin.test)
  commonTestImplementation(Deps.kotlinx.datetime)

  jvmTestImplementation(Deps.junit)
  jvmTestImplementation(Deps.kotlinx.coroutines.test)
}
