plugins {
  id("plugin-multiplatform")
  id("plugin-android-library")
  id("plugin-target-android")
  id("plugin-target-ios")
}

dependencies {
  commonMainImplementation(Deps.kotlin.stdlib.common)
  commonMainImplementation(Deps.kotlinx.coroutines.core)
}

dependencies {
  commonTestImplementation(kotlin("test"))

  androidTestImplementation("junit:junit:4.13.2")
  androidTestImplementation(Deps.kotlinx.coroutines.test)
}
