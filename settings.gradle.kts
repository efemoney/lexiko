@file:Suppress("UnstableApiUsage")

enableFeaturePreview("VERSION_CATALOGS")
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
    exclusiveContent {
      forRepositories(maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap"))
      filter { includeGroupByRegex("^io\\.ktor.*") }
    }
  }
  versionCatalogs {
    register("Deps") {
      from(fileTree("gradle/dependencies"))
    }
  }
}

include("statemachine")
include("server")
include("engine")
include("core")
include("app")
