@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    google {
      content {
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
        includeGroupAndSubgroups("androidx")
      }
    }
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
      content {
        includeGroupAndSubgroups("org.jetbrains.androidx")
        includeGroupAndSubgroups("org.jetbrains.compose")
        includeGroupAndSubgroups("org.jetbrains.skiko")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google {
      content {
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
        includeGroupAndSubgroups("androidx")
      }
    }
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
      content {
        includeGroupAndSubgroups("org.jetbrains.androidx")
        includeGroupAndSubgroups("org.jetbrains.compose")
        includeGroupAndSubgroups("org.jetbrains.skiko")
      }
    }
    mavenCentral()
  }
}

rootProject.name = "lexiko"

//includeBuild("../../OSS/nav3-recipes")

include(
  ":engine-api",
  ":engine-impl",
  ":libs-di",
  ":libs-util",
  ":presentation-api",
  ":presentation-nav3",
  ":presentation-circuit",
  ":ui-common",
  ":ui-lobby",
  ":app-common",
  ":android",
  ":desktop",
  //":iOS",
)

// region Version Catalog as Constraints
gradle.lifecycle.afterProject {
  the<VersionCatalogsExtension>().named("libs").run {
    val allLibs = libraryAliases.mapNotNull { findLibrary(it).get() }
    dependencies.constraints {
      configurations.configureEach {
        allLibs.forEach { add(name, it) }
      }
    }
  }
}
// endregion

// region Auto Project directories
val ProjectDescriptor.descendants: Sequence<ProjectDescriptor>
  get() = children.asSequence().flatMap { sequenceOf(it) + it.descendants }

gradle.settingsEvaluated {
  rootProject.descendants.forEach { project ->
    val path = project.path // Given path - :tooling:idea-plugin OR :tooling-idea-plugin
    val splitPath = path.trim(':').replace(':', '-').split('-') // after splitting - [tooling, idea, plugin]

    // path candidates - [tooling, idea, plugin], [tooling, idea-plugin], [tooling-idea-plugin]
    val filePathCandidates = generateSequence(splitPath) {
      if (it.size <= 1) null else it.dropLast(2) + it.takeLast(2).joinToString("-")
    }

    // first directory that exists becomes project path
    filePathCandidates
      .map { file(it.joinToString(File.separator)) }
      .firstOrNull(File::isDirectory)
      ?.let { project(path).projectDir = it }
  }
}
// endregion
