@file:Suppress("NOTHING_TO_INLINE", "FunctionName", "UnstableApiUsage", "UNCHECKED_CAST")

import Build_gradle.CommonComponentsExtensionT
import Build_gradle.CommonExtensionT
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.squareup.anvil.plugin.AnvilExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Architecture
import org.jetbrains.kotlin.konan.target.Family

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.noarg) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.anvil) apply false
  alias(libs.plugins.molecule) apply false
  alias(libs.plugins.ktor) apply false
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
  val target by lazy { libs.versions.jvmTarget.get() }

  withPlugin("java") {
    configure<JavaPluginExtension> {
      sourceCompatibility = JavaVersion.toVersion(target)
      sourceCompatibility = JavaVersion.toVersion(target)
    }
  }

  withAnyKotlinPlugin {
    kotlin.jvmCompilerOptions {
      jvmTarget = JvmTarget.fromTarget(target)
    }
  }

  withAnyAndroidPlugin {
    android.compileOptions {
      targetCompatibility = JavaVersion.toVersion(target)
      targetCompatibility = JavaVersion.toVersion(target)
    }
  }
}

fun Project.AllTheBoms() {
  configurations.named { "kotlinCompiler" !in it }.configureEach {
    val configuration = this
    dependencies {
      withAnyKotlinPlugin {
        configuration(platform(libs.ktor.bom))
        configuration(platform(libs.kotlin.bom))
        configuration(platform(libs.kotlinx.coroutines.bom))
        configuration(platform(libs.kotlinx.serialization.bom))
      }
      withAnyAndroidPlugin {
        configuration(platform(libs.okio.bom))
        configuration(platform(libs.okHttp.bom))
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
  withAnyAndroidPlugin {
    android {
      compileSdk = 34
      defaultConfig.minSdk = 26
      compileOptions { isCoreLibraryDesugaringEnabled = true }
      dependencies { "coreLibraryDesugaring"(libs.android.tools.desugar) }
    }
  }
}

fun Project.KotlinConvention() {
  withAnyKotlinPlugin {
    kotlin {
      jvmToolchain(libs.versions.jvm.get().toInt())
      compilerOptions {
        verbose = true
        progressiveMode = true
        optIn.appendAll(
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
        freeCompilerArgs.appendAll(
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
      jvmCompilerOptions {
        javaParameters = true
      }
    }
  }
}

fun Project.AnvilConvention() {
  withPlugin("com.squareup.anvil") {
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

  withPlugin("java") {
    the<JavaPluginExtension>().sourceSets.configureEach {
      java.setSrcDirs(listOf(simpleName(name, "src")))
      resources.setSrcDirs(listOf(simpleName(name, "resources")))
    }
  }

  withAnyKotlinPlugin {
    kotlin.sourceSets.configureEach {
      kotlin.setSrcDirs(listOf(simpleName(name, "src")))
      resources.setSrcDirs(listOf(simpleName(name, "resources")))
    }
  }

  withAnyAndroidPlugin {
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
  withPlugin("org.jetbrains.kotlin.plugin.compose") {
    configure<ComposeCompilerGradlePluginExtension> {
      val reporting = the<ReportingExtension>()
      val reportsEnabled = providers.gradleProperty("lexiko.compose.enableReports")
      val reportsDir = reporting.baseDirectory.dir("compose").filter { reportsEnabled.orNull == "true" }
      reportsDestination = reportsDir
      metricsDestination = reportsDir
      enableIntrinsicRemember = true
      enableExperimentalStrongSkippingMode = true
      stabilityConfigurationFile = rootProject.file("compose-stability-config.txt")
    }
    dependencies {
      "implementation"(libs.jetbrains.compose.runtime)
    }
  }
}

fun Project.UseMoshiVersion() {
  // MoshiX add a moshi dependency with version 1.13.0, we override with our own higher version
  withPlugin("dev.zacsweers.moshix") {
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

private typealias CommonExtensionT = CommonExtension<*, *, *, *, *, *>

private typealias CommonComponentsExtensionT<T> = AndroidComponentsExtension<T, *, *>


inline val Project.buildDirectory get() = layout.buildDirectory

inline val ProviderFactory.isCi get() = environmentVariable("CI").map(String::toBoolean).getOrElse(false)

inline fun Project.withAnyImplementationAndApiConfigurations(action: Action<Configuration>) {
  configurations
    .named { it.contains("api", true) || it.contains("implementation", true) }
    .configureEach(action)
}

inline fun Project.withPlugin(plugin: String, action: Action<AppliedPlugin>) =
  pluginManager.withPlugin(plugin, action)

inline fun Project.withAnyPlugin(vararg plugins: String, action: Action<AppliedPlugin>) =
  plugins.forEach { withPlugin(it, action) }

inline fun Project.withAnyKotlinPlugin(action: Action<AppliedKotlinPlugin<KotlinProjectExtension>>) {
  withAnyPlugin(
    "org.jetbrains.kotlin.jvm",
    "org.jetbrains.kotlin.android",
    "org.jetbrains.kotlin.multiplatform",
  ) { action.execute(AppliedKotlinPlugin(this)) }
}

inline fun Project.withAnyAndroidPlugin(
  action: Action<AppliedAndroidPlugin<CommonExtensionT, CommonComponentsExtensionT<CommonExtensionT>>>
) {
  withAnyPlugin("com.android.application", "com.android.library") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

inline fun Project.withAndroidLibrary(
  action: Action<AppliedAndroidPlugin<LibraryExtension, LibraryAndroidComponentsExtension>>
) {
  withPlugin("com.android.library") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

inline fun Project.withKotlinMultiplatformPlugin(action: Action<AppliedKotlinPlugin<KotlinMultiplatformExtension>>) {
  withPlugin("org.jetbrains.kotlin.multiplatform") {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun Project.withKotlinJvmOrAndroidPlugins(action: Action<AppliedKotlinPlugin<KotlinProjectExtension>>) {
  withAnyPlugin("org.jetbrains.kotlin.jvm", "org.jetbrains.kotlin.android") {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun Configuration.withDependency(
  dependency: Provider<MinimalExternalModuleDependency>,
  action: Action<ExternalModuleDependency>,
) {
  incoming.dependencies.configureEach {
    if (this is ExternalModuleDependency && this.module == dependency.get().module) action(this)
  }
}

inline fun Configuration.withDependency(
  dependency: ProviderConvertible<MinimalExternalModuleDependency>,
  action: Action<ExternalModuleDependency>,
) = withDependency(dependency.asProvider(), action)

class AppliedAndroidPlugin<T : CommonExtensionT, U : CommonComponentsExtensionT<T>>(
  appliedPlugin: AppliedPlugin
) : AppliedPlugin by appliedPlugin {

  inline val Project.android get() = extensions.getByName("android") as T

  inline val Project.androidComponents get() = extensions.getByName("androidComponents") as U

  inline fun Project.android(action: Action<T>) = extensions.configure("android", action)

  inline fun Project.androidComponents(action: Action<U>) = extensions.configure("androidComponents", action)
}

class AppliedKotlinPlugin<T : KotlinProjectExtension>(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {

  inline val Project.kotlin get() = extensions.getByName("kotlin") as T

  inline fun Project.kotlin(action: Action<T>) = extensions.configure("kotlin", action)

  inline fun KotlinProjectExtension.compilerOptions(action: Action<KotlinCommonCompilerOptions>) {
    if (this is KotlinJvmProjectExtension) action.execute(compilerOptions)
    if (this is KotlinAndroidProjectExtension) action.execute(compilerOptions)
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    if (this is KotlinMultiplatformExtension) action.execute(compilerOptions)
  }

  inline fun KotlinProjectExtension.jvmCompilerOptions(action: Action<KotlinJvmCompilerOptions>) {
    if (this is KotlinJvmProjectExtension) compilerOptions(action)
    if (this is KotlinAndroidProjectExtension) compilerOptions(action)
  }

  inline fun KotlinMultiplatformExtension.nativeTargets(
    family: Family? = null,
    architecture: Architecture? = null,
    action: Action<KotlinNativeTarget>,
  ) {
    targets.withType<KotlinNativeTarget>()
      .matching {
        (family == null || it.konanTarget.family == family) &&
          (architecture == null || it.konanTarget.architecture == architecture)
      }
      .configureEach(action)
  }

  inline fun KotlinMultiplatformExtension.iosTargets(action: Action<KotlinNativeTarget>) {
    nativeTargets(Family.IOS, action = action)
  }
}

// endregion
