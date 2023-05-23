@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.support.serviceOf

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    mavenCentral()
    google()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
      mavenContent { snapshotsOnly() }
    }
    gradlePluginPortal()
  }
}

plugins {
  id("com.gradle.enterprise") version "3.13.2"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    val exec = serviceOf<ExecOperations>() // Necessary for configuration cache
    buildScanPublished {
      exec.exec { commandLine("open", buildScanUri) }
    }
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
    maven("https://androidx.dev/storage/compose-compiler/repository/") {
      content {
        includeModule("androidx.compose.compiler", "compiler")
      }
    }
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
      mavenContent { snapshotsOnly() }
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

rootProject.name = "lexiko"
