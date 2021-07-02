@file:Suppress("UnstableApiUsage", "FunctionName")

import com.github.benmanes.gradle.versions.VersionsPlugin

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

gradle.rootProject {
  apply<VersionsPlugin>() // ben-manes
}

gradle.beforeProject {
  AllTheBoms()
  JavaConvention()
  KotlinConvention()
  KaptConvention()
  AndroidConvention()
}
