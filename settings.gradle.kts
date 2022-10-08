@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

plugins {
  id("com.gradle.develocity") version "3.17"
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

develocity.buildScan {
  termsOfUseUrl = "https://gradle.com/terms-of-service"
  termsOfUseAgree = "yes"
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
    maven("https://androidx.dev/storage/compose-compiler/repository/") {
      content { includeModule("androidx.compose.compiler", "compiler") }
    }
  }
}

include("dubbed:compiler-plugin")
include("dubbed:gradle-plugin")
include("statemachine")
include("libs:di:scopes")
include("libs:di:codegen")
include("libs:navigation")
include("libs:ui")
include("ui:board")

include("server")

include("engine")
include("engine:api")
include("engine:impl")

include("app:android")
include("app:iOS")
include("app:common")

rootProject.name = "lexiko"
