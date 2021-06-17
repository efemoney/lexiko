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

include("core")
include("engine")
include("android")
include("ios")
include("server")
