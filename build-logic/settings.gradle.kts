@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

include("src")
include("plugin-android")
include("plugin-simple-layout")
include("plugin-multiplatform")
