@file:Suppress("UnstableApiUsage", "FunctionName")

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.LibraryDefaultConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

plugins { id("com.github.ben-manes.versions") version "0.39.0" }

subprojects {
  AllTheBoms()
  JavaConvention()
  TestsConvention()
  AndroidConvention()
  KaptConvention()
  KotlinConvention()
}

fun Project.AllTheBoms() {
  configurations.matching { it.name.contains("implementation", ignoreCase = true) }.configureEach {
    val implementation = this

    dependencies {
      pluginManager.withAnyKotlinPlugin {
        //implementation(platform(Deps.ktor.bom))
        implementation(platform(Deps.kotlin.bom))
        implementation(platform(Deps.kotlinx.coroutines.bom))
        implementation(platform(Deps.kotlinx.serialization.bom))
      }

      pluginManager.withAnyAndroidPlugin {
        implementation(platform(Deps.okHttp.bom))
      }

      pluginManager.withMultiplatformPlugin {
        implementation(platform(Deps.okio.bom))
      }
    }
  }
}

fun Project.JavaConvention() {
  pluginManager.withPlugin("java") {
    the<JavaPluginExtension>().toolchain.languageVersion.set(JavaLanguageVersion.of(11))
  }
}

fun Project.TestsConvention() {
  tasks.withType<Test>().configureEach {
    useJUnit()
    testLogging.showStandardStreams = true
  }
}

fun Project.AndroidConvention() {

  pluginManager.withAnyAndroidPlugin {
    android {
      compileSdk = 31
      testCoverage.jacocoVersion = "0.8.7"

      defaultConfig {
        minSdk = 21
        if (this is LibraryDefaultConfig) targetSdk = 30
        if (this is ApplicationDefaultConfig) targetSdk = 30
        vectorDrawables.useSupportLibrary = true
      }

      compileOptions {
        dependencies { "coreLibraryDesugaring"("com.android.tools:desugar_jdk_libs:1.1.5") }
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
      }
    }
    androidComponents.finalizeDsl {
      if (it.buildFeatures.compose == true) {
        it.composeOptions.kotlinCompilerExtensionVersion = Deps.versions.androidx.compose.asProvider().get()
      }
    }
  }
}

fun Project.KaptConvention() {
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

fun Project.KotlinConvention() {
  pluginManager.withAnyKotlinPlugin {
    kotlin {
      sourceSets.configureEach {
        languageSettings {
          progressiveMode = true

          listOf(
            "kotlin.RequiresOptIn",
            "kotlin.ExperimentalStdlibApi",
            "kotlin.ExperimentalMultiplatform",
            "kotlin.time.ExperimentalTime",
            "kotlin.experimental.ExperimentalTypeInference",
            "kotlinx.coroutines.FlowPreview",
            "kotlinx.coroutines.ExperimentalCoroutinesApi",
            "coil.annotation.ExperimentalCoilApi",
            "dev.efemoney.lexiko.engine.internal.InternalEngineApi",
          ).forEach(::optIn)

          listOf(
            "InlineClasses",
            "UnitConversion",
          ).forEach(::enableLanguageFeature)
        }
      }

      when (this) {
        is KotlinSingleTargetExtension -> target.configureFreeArgs()
        is KotlinMultiplatformExtension -> targets.configureEach { configureFreeArgs() }
      }
    }
  }
}

fun KotlinTarget.configureFreeArgs() = compilations.configureEach {

  kotlinOptions {
    verbose = true
    freeCompilerArgs = freeCompilerArgs + listOf(
      "-Xself-upper-bound-inference",
      "-Xunrestricted-builder-inference",
    )

    if (this is KotlinJvmOptions) {
      jvmTarget = "11"
      javaParameters = true
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-Xassertions=jvm", // https://publicobject.com/2019/11/18/kotlins-assert-is-not-like-javas-assert/
        "-Xemit-jvm-type-annotations", // useful for static analysis tools or annotation processors.
        "-Xjvm-default=all", // https://blog.jetbrains.com/kotlin/2020/07/kotlin-1-4-m3-generating-default-methods-in-interfaces/
        "-Xstrict-java-nullability-assertions",
      )
    }
  }
}

tasks.dependencyUpdates {
  rejectVersionIf { "ide" in candidate.version }
}
