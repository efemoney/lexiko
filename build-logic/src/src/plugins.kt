@file:Suppress("ReplaceWithOperatorAssignment", "UnstableApiUsage")

import com.github.benmanes.gradle.versions.VersionsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.buildscript
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.fileTree
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

class LexikoSettingsPlugin : Plugin<Settings> {

  override fun apply(target: Settings) {
    target.apply<VersionCatalogFixPlugin>()
    target.gradle.rootProject {
      apply<KotlinCompilerArgsPlugin>()
      apply<AndroidMultiplatformFixPlugin>()
      apply<VersionsPlugin>()
      apply<AllTheBomsPlugin>()
    }
  }
}

abstract class AllProjectsPlugin(private val applyAction: Project.() -> Unit) : Plugin<Project> {

  override fun apply(target: Project) {
    target.applyAction()
    target.subprojects {
      pluginManager.apply(this@AllProjectsPlugin::class)
    }
  }
}

class AndroidMultiplatformFixPlugin : AllProjectsPlugin({
  pluginManager.withPlugin("kotlin-multiplatform") {
    pluginManager.withAnyAndroidPlugin {
      with(configurations) {
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
      }
    }
  }
})

class KotlinCompilerArgsPlugin : AllProjectsPlugin({
  // Cannot use pluginManager as we need this to be lazy and based on KotlinBasePluginWrapper type
  plugins.withType<KotlinBasePluginWrapper>().configureEach {
    when (val kotlin = extensions["kotlin"] as KotlinProjectExtension) {
      is KotlinSingleTargetExtension -> kotlin.target.configureTargetCompilerArgs()
      is KotlinMultiplatformExtension -> kotlin.targets.configureEach { configureTargetCompilerArgs() }
    }
  }
})

private fun KotlinTarget.configureTargetCompilerArgs() = compilations.configureEach {
  kotlinOptions {
    verbose = true
    freeCompilerArgs = freeCompilerArgs + listOf(
      "-Xinline-classes",
      "-Xmulti-platform",
      "-Xproper-ieee754-comparisons",
      "-Xopt-in=kotlin.RequiresOptIn",
      "-Xopt-in=kotlin.ExperimentalStdlibApi",
      "-Xopt-in=kotlin.time.ExperimentalTime",
      "-Xopt-in=kotlinx.coroutines.FlowPreview",
      "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    )

    if (this is KotlinJvmOptions) {
      useIR = true
      jvmTarget = "1.8"

      freeCompilerArgs = freeCompilerArgs + listOf(
        "-Xassertions=jvm", // https://publicobject.com/2019/11/18/kotlins-assert-is-not-like-javas-assert/
        "-Xemit-jvm-type-annotations", // useful for static analysis tools or annotation processors.
        "-Xjvm-default=all", // https://blog.jetbrains.com/kotlin/2020/07/kotlin-1-4-m3-generating-default-methods-in-interfaces/
        "-Xstrict-java-nullability-assertions",
      )
    }
  }
}

class VersionCatalogFixPlugin : Plugin<Settings> {

  override fun apply(target: Settings) {
    target.enableFeaturePreview("VERSION_CATALOGS")
    target.gradle.rootProject {
      val accessors = files(serviceOf<DependenciesAccessors>().classes.asFiles)
      buildscript { dependencies.classpath(accessors) }
    }
  }
}

class AllTheBomsPlugin : AllProjectsPlugin({

  pluginManager.withAnyPlugin("java", "kotlin") {
    dependencies {
      "implementation"(platform(Deps.kotlin.bom))
      "implementation"(platform(Deps.kotlinx.coroutines.bom))
    }
  }

  pluginManager.withAnyAndroidPlugin {
    dependencies {
      "implementation"(platform(Deps.okHttp.bom))
    }
  }
})
