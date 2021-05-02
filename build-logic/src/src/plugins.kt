@file:Suppress("UnstableApiUsage", "FunctionName")

import com.github.benmanes.gradle.versions.VersionsPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.FeaturePreviews
import org.gradle.api.internal.FeaturePreviews.Feature.VERSION_CATALOGS
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.buildscript
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import javax.inject.Inject

class LexikoSettingsPlugin : Plugin<Settings> {

  override fun apply(target: Settings) {
    target.apply<VersionCatalogFixPlugin>()
    target.gradle.rootProject {
      apply<VersionsPlugin>()

      AndroidMultiplatformFix()
      JavaConvention()
      KotlinConvention()
      KaptConvention()
      AllTheBoms()
    }
  }
}

class VersionCatalogFixPlugin @Inject constructor(private val featurePreviews: FeaturePreviews) : Plugin<Settings> {

  override fun apply(target: Settings) {
    if (featurePreviews.isFeatureEnabled(VERSION_CATALOGS))
      target.gradle.rootProject {
        val accessors = files(serviceOf<DependenciesAccessors>().classes.asFiles)
        buildscript { dependencies.classpath(accessors) }
      }
  }
}

private fun Project.AllTheBoms() = subprojects {

  pluginManager.withAnyPlugin("java", "kotlin") {
    dependencies {
      "implementation"(platform(Deps.ktor.bom))
      "implementation"(platform(Deps.kotlin.bom))
      "implementation"(platform(Deps.kotlinx.coroutines.bom))
    }
  }

  pluginManager.withAnyAndroidPlugin {
    dependencies {
      "implementation"(platform(Deps.okHttp.bom))
    }
  }
}

private fun Project.AndroidMultiplatformFix() = subprojects {
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
}

private fun Project.JavaConvention() = subprojects {
  pluginManager.withAnyPlugin("java", "kotlin") {
    configure<JavaPluginExtension> {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11
    }
  }
}

private fun Project.KaptConvention() = subprojects {
  pluginManager.withPlugin("kotlin-kapt") {
    configure<KaptExtension> {
      correctErrorTypes = true
      showProcessorTimings = true
      mapDiagnosticLocations = true
      arguments {
        arg("dagger.fastInit", "enabled")
        arg("dagger.experimentalDaggerErrorMessages", "enabled")
      }
    }
  }
}

private fun Project.KotlinConvention() = subprojects {
  // Cannot use pluginManager as we need this to be lazy and based on KotlinBasePluginWrapper type
  plugins.withType<KotlinBasePluginWrapper>().configureEach {

    configure<KotlinProjectExtension> {

      val optIns = listOf(
        "kotlin.RequiresOptIn",
        "kotlin.ExperimentalStdlibApi",
        "kotlin.ExperimentalMultiplatform",
        "kotlin.time.ExperimentalTime",
        "kotlinx.coroutines.FlowPreview",
        "kotlinx.coroutines.ExperimentalCoroutinesApi",
      )

      val languageFeatures = listOf(
        "InlineClasses",
        "UnitConversion",
      )

      sourceSets.configureEach {
        optIns.forEach(languageSettings::useExperimentalAnnotation)
        languageFeatures.forEach(languageSettings::enableLanguageFeature)
      }

      when (this) {
        is KotlinSingleTargetExtension -> target.configureFreeArgs()
        is KotlinMultiplatformExtension -> targets.configureEach { configureFreeArgs() }
      }
    }
  }
}

private fun KotlinTarget.configureFreeArgs() = compilations.configureEach {

  val freeArgs = listOf("-progressive")

  val jvmFreeArgs = listOf(
    "-Xassertions=jvm", // https://publicobject.com/2019/11/18/kotlins-assert-is-not-like-javas-assert/
    "-Xemit-jvm-type-annotations", // useful for static analysis tools or annotation processors.
    "-Xjvm-default=all", // https://blog.jetbrains.com/kotlin/2020/07/kotlin-1-4-m3-generating-default-methods-in-interfaces/
    "-Xstrict-java-nullability-assertions",
  )

  kotlinOptions {
    verbose = true
    freeCompilerArgs = freeCompilerArgs + freeArgs

    if (this is KotlinJvmOptions) {
      useIR = true
      jvmTarget = "1.8"
      javaParameters = true
      freeCompilerArgs = freeCompilerArgs + jvmFreeArgs
    }
  }
}
