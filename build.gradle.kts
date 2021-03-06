@file:Suppress("DSL_SCOPE_VIOLATION", "NOTHING_TO_INLINE", "UNCHECKED_CAST", "FunctionName")

import Build_gradle.CommonExtensionT
import Build_gradle.ComponentsExtensionT
import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.variant.AndroidComponentsExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

plugins {
  alias(Deps.plugins.android.application) apply false
  alias(Deps.plugins.android.library) apply false
  alias(Deps.plugins.kotlin.android) apply false
  alias(Deps.plugins.kotlin.jvm) apply false
  alias(Deps.plugins.kotlin.noarg) apply false
  alias(Deps.plugins.kotlin.kapt) apply false
  id("com.github.ben-manes.versions") version "0.42.0"
}

subprojects {
  AllTheBoms()
  JavaConvention()
  TestsConvention()
  AndroidConvention()
  KaptConvention()
  KotlinConvention()
  SimpleLayoutConvention()
}

// region Conventions

fun Project.AllTheBoms() {
  configurations.matching { it.name.contains("implementation", ignoreCase = true) }.configureEach {
    val implementation = this

    dependencies {
      pluginManager.withAnyKotlinPlugin {
        implementation(platform(Deps.ktor.bom))
        implementation(platform(Deps.kotlin.bom))
        implementation(platform(Deps.kotlinx.coroutines.bom))
        implementation(platform(Deps.kotlinx.serialization.bom))
      }

      pluginManager.withAnyAndroidPlugin {
        implementation(platform(Deps.okio.bom))
        implementation(platform(Deps.okHttp.bom))
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

      testCoverage { jacocoVersion = "0.8.8" }
    }
    androidComponents.finalizeDsl {
      if (it.buildFeatures.compose == true) it.composeOptions {
        kotlinCompilerExtensionVersion = Deps.versions.androidx.compose.compiler.get()
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

    tasks.withType<KotlinCompile<*>>().configureEach {
      kotlinOptions {
        verbose = true
        freeCompilerArgs = freeCompilerArgs + listOf(
          "-progressive",

          "-opt-in=kotlin.RequiresOptIn",
          "-opt-in=kotlin.ExperimentalStdlibApi",
          "-opt-in=kotlin.ExperimentalMultiplatform",
          "-opt-in=kotlin.time.ExperimentalTime",
          "-opt-in=kotlin.experimental.ExperimentalTypeInference",
          "-opt-in=kotlinx.coroutines.FlowPreview",
          "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-opt-in=coil.annotation.ExperimentalCoilApi",
          "-opt-in=dev.efemoney.lexiko.engine.internal.InternalEngineApi",

          "-Xnew-inference",
          "-Xinline-classes",
          "-Xself-upper-bound-inference",
          "-Xunrestricted-builder-inference",

          "-Xjsr305=strict",
          "-Xassertions=jvm", // https://publicobject.com/2019/11/18/kotlins-assert-is-not-like-javas-assert/
          "-Xemit-jvm-type-annotations", // useful for static analysis tools or annotation processors.
          "-Xjvm-default=all", // https://blog.jetbrains.com/kotlin/2020/07/kotlin-1-4-m3-generating-default-methods-in-interfaces/
          "-Xproper-ieee754-comparisons",
        )

        if (this is KotlinJvmCompile) kotlinOptions {
          jvmTarget = "11"
          javaParameters = true
        }
      }
    }
  }
}

fun Project.SimpleLayoutConvention() {

  pluginManager.withPlugin("java") {
    the<JavaPluginExtension>().sourceSets.configureEach {
      java.setSrcDirs(listOf(simpleName(name, "src")))
      resources.setSrcDirs(listOf(simpleName(name, "resources")))
    }
  }

  pluginManager.withAnyKotlinPlugin {
    kotlin.sourceSets.configureEach {
      kotlin.setSrcDirs(listOf(simpleName(name, "src")))
      resources.setSrcDirs(listOf(simpleName(name, "resources")))
    }
  }

  pluginManager.withAnyAndroidPlugin {
    android.sourceSets.configureEach {
      manifest.srcFile("$name/AndroidManifest.xml")
      java.setSrcDirs(listOf(simpleName(name, "src")))
      kotlin.setSrcDirs(listOf(simpleName(name, "src")))
      res.setSrcDirs(listOf(simpleName(name, "res")))
      resources.setSrcDirs(listOf(simpleName(name, "resources")))
      assets.setSrcDirs(listOf(simpleName(name, "assets")))
      aidl.setSrcDirs(listOf(simpleName(name, "aidl")))
      renderscript.setSrcDirs(listOf(simpleName(name, "renderscript")))
      jniLibs.setSrcDirs(listOf(simpleName(name, "jniLibs")))
      shaders.setSrcDirs(listOf(simpleName(name, "shaders")))
      mlModels.setSrcDirs(listOf(simpleName(name, "mlModels")))
    }
  }
}

fun simpleName(name: String, suffix: String) = if (name == "main") suffix else "$name-$suffix"

// endregion

tasks {
  register<Delete>("clean") {
    delete(rootProject.buildDir)
  }
  dependencyUpdates {
    rejectVersionIf { "ide" in candidate.version || "dev" in candidate.version }
  }
}

// region DSL

private typealias CommonExtensionT = CommonExtension<*, *, *, *>

private typealias ComponentsExtensionT = AndroidComponentsExtension<CommonExtensionT, *, *>

inline fun PluginManager.withAnyPlugin(vararg plugins: String, action: Action<AppliedPlugin>) =
  plugins.forEach { withPlugin(it, action) }

inline fun PluginManager.withAnyKotlinPlugin(action: Action<AppliedKotlinPlugin<KotlinProjectExtension>>) {
  withAnyPlugin(
    "org.jetbrains.kotlin.js",
    "org.jetbrains.kotlin.jvm",
    "org.jetbrains.kotlin.android",
    "org.jetbrains.kotlin.multiplatform",
  ) {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun PluginManager.withMultiplatformPlugin(action: Action<AppliedKotlinPlugin<KotlinMultiplatformExtension>>) {
  withPlugin("org.jetbrains.kotlin.multiplatform") {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun PluginManager.withAnyAndroidPlugin(action: Action<AppliedAndroidPlugin>) {
  withAnyPlugin(
    "com.android.application",
    "com.android.library",
  ) {
    action.execute(AppliedAndroidPlugin(this))
  }
}


class AppliedAndroidPlugin(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {

  inline val Project.android get() = extensions.getByName<CommonExtensionT>("android")

  inline fun Project.android(action: Action<CommonExtensionT>) = extensions.configure("android", action)

  inline val Project.androidComponents
    get() = extensions.getByName<ComponentsExtensionT>("androidComponents")

  inline fun Project.androidComponents(action: Action<ComponentsExtensionT>) =
    extensions.configure("androidComponents", action)
}

class AppliedKotlinPlugin<T : KotlinProjectExtension>(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {

  inline val Project.kotlin get() = extensions.getByName<KotlinProjectExtension>("kotlin") as T

  inline fun Project.kotlin(action: Action<T>) = extensions.configure("kotlin", action)
}

// endregion
