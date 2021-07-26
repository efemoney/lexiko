@file:Suppress("FunctionName")

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

internal fun Project.KaptConvention() {
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

internal fun Project.KotlinConvention() {
  pluginManager.withAnyKotlinPlugin {
    kotlin {
      val optIns = listOf(
        "kotlin.RequiresOptIn",
        "kotlin.ExperimentalStdlibApi",
        "kotlin.ExperimentalMultiplatform",
        "kotlin.experimental.ExperimentalTypeInference",
        "kotlin.time.ExperimentalTime",
        "kotlinx.coroutines.FlowPreview",
        "kotlinx.coroutines.ExperimentalCoroutinesApi",
        "coil.annotation.ExperimentalCoilApi",
        "dev.efemoney.lexiko.engine.internal.InternalEngineApi",
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
      jvmTarget = "11"
      javaParameters = true
      freeCompilerArgs = freeCompilerArgs + jvmFreeArgs
    }
  }
}
