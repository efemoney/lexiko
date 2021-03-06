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
  `gradle-enterprise`
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    buildScanPublished {
      exec { commandLine("open", buildScanUri) }
    }
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
  }
  versionCatalogs {
    register("Deps") {
      from(fileTree("gradle/dependencies"))
    }
  }
}

include("dubbed:compiler-plugin")
include("dubbed:gradle-plugin")
include("statemachine")
include("server")
include("engine")
include("core")
include("app")
