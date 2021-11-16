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
    val ktor = maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")

    exclusiveContent {
      forRepositories(ktor)
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
include("engine")
include("core")
include("android")
include("ios")
include("server")
