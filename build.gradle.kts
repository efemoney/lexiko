@file:Suppress("NOTHING_TO_INLINE", "FunctionName", "UnstableApiUsage")

import Build_gradle.CommonExtensionT
import Build_gradle.ComponentsExtensionT
import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.squareup.anvil.plugin.AnvilExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.noarg) apply false
  alias(libs.plugins.anvil) apply false
  alias(libs.plugins.versions)
}

subprojects {
  JvmTargetConvention()
  AllTheBoms()
  TestsConvention()
  AndroidConvention()
  KotlinConvention()
  AnvilConvention()
  ComposeConvention()
  UseMoshiVersion()
  SimpleLayoutConvention()
}

// region Conventions

fun Project.JvmTargetConvention() {

  fun <T : Any> jvmTarget(map: (String) -> T) = libs.versions.jvmTarget.map(map).get()

  pluginManager.withAnyKotlinPlugin {
    tasks.withType<KotlinJvmCompile>().configureEach {
      compilerOptions {
        jvmTarget.set(jvmTarget(JvmTarget::fromTarget))
      }
    }
  }

  pluginManager.withPlugin("java") {
    configure<JavaPluginExtension> {
      val target = jvmTarget(JavaVersion::toVersion)
      sourceCompatibility = target
      targetCompatibility = target
    }
  }

  pluginManager.withAnyAndroidPlugin {
    android.compileOptions {
      val target = jvmTarget(JavaVersion::toVersion)
      sourceCompatibility = target
      targetCompatibility = target
    }
  }
}

fun Project.AllTheBoms() {
  configurations
    .matching { "kotlinCompiler" !in it.name }
    .configureEach {
      val configuration = this

      dependencies {
        pluginManager.withAnyKotlinPlugin {
          configuration(platform(libs.ktor.bom))
          configuration(platform(libs.kotlin.bom))
          configuration(platform(libs.kotlinx.coroutines.bom))
          configuration(platform(libs.kotlinx.serialization.bom))
        }

        pluginManager.withAnyAndroidPlugin {
          configuration(platform(libs.okio.bom))
          configuration(platform(libs.okHttp.bom))
        }
      }
    }

  configurations.configureEach {
    resolutionStrategy.eachDependency {
      if (requested.group == libs.kotlinpoet.ksp.get().group) {
        useVersion(libs.versions.kotlinpoet.get())
      }
    }
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
      compileSdk = 33

      defaultConfig {
        minSdk = 26
        if (this is ApplicationDefaultConfig) targetSdk = 33
        vectorDrawables.useSupportLibrary = true
      }

      compileOptions {
        dependencies { "coreLibraryDesugaring"(libs.android.tools.desugar) }
        isCoreLibraryDesugaringEnabled = true
      }
    }
    androidComponents.finalizeDsl {
      if (it.buildFeatures.compose == true) it.composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
      }
    }
  }
}

fun Project.KotlinConvention() {
  pluginManager.withAnyKotlinPlugin {
    kotlin {
      jvmToolchain(libs.versions.jvm.get().toInt())
      if (this is KotlinJvmProjectExtension) compilerOptions.configure()
      if (this is KotlinAndroidProjectExtension) compilerOptions.configure()
    }
  }
}

fun KotlinJvmCompilerOptions.configure() {
  apiVersion = KOTLIN_1_9
  languageVersion = KOTLIN_1_9
  verbose = true
  javaParameters = true
  progressiveMode = true
  optIn.addAll(
    "kotlin.RequiresOptIn",
    "kotlin.ExperimentalStdlibApi",
    "kotlin.ExperimentalMultiplatform",
    "kotlin.time.ExperimentalTime",
    "kotlin.experimental.ExperimentalTypeInference",
    "kotlinx.coroutines.FlowPreview",
    "kotlinx.coroutines.ExperimentalCoroutinesApi",
    "coil.annotation.ExperimentalCoilApi",
    "dev.efemoney.lexiko.engine.internal.InternalEngineApi",
  )
  freeCompilerArgs.addAll(
    "-XXLanguage:+ValueClasses",
    "-XXLanguage:+ContextReceivers",
    "-XXLanguage:+BreakContinueInInlineLambdas",
    "-XXLanguage:+UnitConversionsOnArbitraryExpressions",
    "-XXLanguage:+PackagePrivateFileClassesWithAllPrivateMembers", // KT-10884

    "-Xself-upper-bound-inference",
    "-Xunrestricted-builder-inference",
    "-Xjsr305=strict",
    "-Xassertions=jvm", // https://publicobject.com/2019/11/18/kotlins-assert-is-not-like-javas-assert/
    "-Xjvm-default=all", // https://blog.jetbrains.com/kotlin/2020/07/kotlin-1-4-m3-generating-default-methods-in-interfaces/
    "-Xemit-jvm-type-annotations", // useful for static analysis tools or annotation processors.
    "-Xproper-ieee754-comparisons",
  )
}

fun Project.AnvilConvention() {
  pluginManager.withPlugin("com.squareup.anvil") {
    configure<AnvilExtension> {
      // We configure Anvil for modules that do not @MergeComponents to:
      // - generate factories, so that we can skip dagger compiler entirely for the module
      // - we also disable component merging: the idea here is that modules either merge components
      //   or they contribute dependencies, never both.
      val mergesComponent = project.name in listOf("integration")
      generateDaggerFactories = !mergesComponent
      disableComponentMerging = !mergesComponent
      /*syncGeneratedSources = true*/
    }
    dependencies {
      "anvil"(projects.libs.di.codegen)
      "implementation"(libs.dagger)
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
      // AS & IntelliJ map (roughly) a source set to a 'module'
      // This is important because in AS, whatever folder the manifest.xml file lives in becomes a source root
      // according to this comment https://issuetracker.google.com/issues/232007221#comment13
      // We keep manifest files in their own unique folders so intellij does not choke on "duplicate source roots"
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
      baselineProfiles.setSrcDirs(listOf(simpleName(name, "baselineProfiles")))
    }
  }
}

fun Project.ComposeConvention() {
  // Molecule depends on an older compose compiler, we force our own compiler version instead
  configurations.configureEach {
    resolutionStrategy.eachDependency {
      if (requested.module == libs.androidx.compose.compiler.get().module)
        useVersion(libs.versions.androidx.compose.compiler.get())
    }
  }

  pluginManager.withAnyKotlinPlugin {
    kotlin {
      if (this is KotlinJvmProjectExtension) compilerOptions.configureCompose()
      if (this is KotlinAndroidProjectExtension) compilerOptions.configureCompose()
    }
  }
}

fun KotlinJvmCompilerOptions.configureCompose() {
  // Enable compiler metrics if requested
  if (providers.gradleProperty("lexiko.compose.enableCompilerReports").isPresent) {
    val metricsDir = layout.buildDirectory.dir("compose-metrics").get().asFile
    freeCompilerArgs.addAll(
      "-P", "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$metricsDir",
      "-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$metricsDir",
    )
  }
}

fun Project.UseMoshiVersion() {
  // MoshiX add a moshi dependency with version 1.13.0, we override with our own higher version
  pluginManager.withPlugin("dev.zacsweers.moshix") {
    dependencies {
      "implementation"(libs.moshi)
    }
  }
}

fun simpleName(name: String, suffix: String) = if (name == "main") suffix else "$name-$suffix"

// endregion

tasks {
  register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
  }
  dependencyUpdates {
    rejectVersionIf { "ide" in candidate.version || "dev" in candidate.version }
  }
}

// region DSL

private typealias CommonExtensionT = CommonExtension<*, *, *, *, *>

private typealias ComponentsExtensionT = AndroidComponentsExtension<CommonExtensionT, *, *>

inline fun PluginManager.withPlugin(plugin: PluginDependency, action: Action<AppliedPlugin>) =
  withPlugin(plugin.pluginId, action)

inline fun PluginManager.withPlugin(plugin: Provider<PluginDependency>, action: Action<AppliedPlugin>) =
  withPlugin(plugin.get(), action)

inline fun PluginManager.withAnyPlugin(vararg plugins: String, action: Action<AppliedPlugin>) =
  plugins.forEach { withPlugin(it, action) }

inline fun PluginManager.withAnyKotlinPlugin(action: Action<AppliedKotlinPlugin>) {
  withAnyPlugin("org.jetbrains.kotlin.jvm", "org.jetbrains.kotlin.android") {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun PluginManager.withAnyAndroidPlugin(action: Action<AppliedAndroidPlugin>) {
  withAnyPlugin("com.android.application", "com.android.library") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

inline fun PluginManager.withAndroidLibPlugin(action: Action<AppliedAndroidPlugin>) {
  withPlugin("com.android.library") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

class AppliedAndroidPlugin(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {

  inline val Project.android
    get() = extensions.getByName<CommonExtensionT>("android")

  inline fun Project.android(action: Action<CommonExtensionT>) =
    extensions.configure("android", action)

  inline val Project.androidComponents
    get() = extensions.getByName<ComponentsExtensionT>("androidComponents")

  inline fun Project.androidComponents(action: Action<ComponentsExtensionT>) =
    extensions.configure("androidComponents", action)
}

class AppliedKotlinPlugin(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {
  inline val Project.kotlin
    get() = extensions.getByName<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension>("kotlin")

  inline fun Project.kotlin(action: Action<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension>) =
    extensions.configure("kotlin", action)
}

// endregion
