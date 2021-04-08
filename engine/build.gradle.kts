plugins {
  id("plugin-multiplatform")
  id("plugin-android-target")
  id("plugin-ios-target")
}

dependencies {
  commonMainImplementation(Deps.kotlin.stdlib.common)
  commonMainImplementation(Deps.kotlinx.coroutines.core)
}

dependencies {
  commonTestImplementation(kotlin("test-common"))
  commonTestImplementation(kotlin("test-annotations-common"))

  androidTestImplementation(kotlin("test-junit"))
  androidTestImplementation("junit:junit:4.13.2")
}
