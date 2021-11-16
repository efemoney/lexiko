@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  versionCatalogs {
    register("Deps") {
      from(fileTree("../gradle/dependencies"))
    }
  }
}
