@file:Suppress("UnstableApiUsage")

pluginManagement {

  includeBuild("build-logic")

  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("plugin-lexiko-settings")
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

include("core")
include("engine")
include("android")
include("server")
