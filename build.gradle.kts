@file:Suppress("NOTHING_TO_INLINE", "FunctionName", "UnstableApiUsage", "UNCHECKED_CAST")
@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalSwiftExportDsl::class)

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.google.devtools.ksp.gradle.KspExtension
import dev.zacsweers.metro.gradle.DiagnosticSeverity
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.swiftexport.SwiftExportExtension
import org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl
import org.jetbrains.kotlin.konan.target.Architecture
import org.jetbrains.kotlin.konan.target.Family

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.multiplatform) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.noarg) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.compose.multiplatform) apply false
  alias(libs.plugins.ddependencyGuard) apply false
  alias(libs.plugins.spotlight) apply false
  alias(libs.plugins.conveyor) apply false
  alias(libs.plugins.anvil) apply false
  alias(libs.plugins.poko) apply false
  alias(libs.plugins.ktor) apply false
  alias(libs.plugins.metro) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.versions)
  alias(libs.plugins.idea.ext)
}

subprojects {
  AllTheBoms()
  JvmTargetConvention()
  KotlinConvention()
  AndroidConvention()
  IosConvention()
  KspConvention()
  MetroConvention()
  ComposeConvention()
  LintConvention()
}

// region Conventions

fun Project.AllTheBoms() {
  withConfigurations("implementation") {
    dependencies {
      it(platform(libs.kotlin.bom))
      it(platform(libs.kotlinx.coroutines.bom))
      it(platform(libs.kotlinx.serialization.bom))
      //it(platform(libs.ktor.bom))
      //it(platform(libs.okio.bom))
      //it(platform(libs.okHttp.bom))
    }
  }
}

fun Project.JvmTargetConvention() {
  val target by lazy { libs.versions.jvmTarget.get() }

  withPlugin("java") {
    configure<JavaPluginExtension> {
      sourceCompatibility = JavaVersion.toVersion(target)
      targetCompatibility = JavaVersion.toVersion(target)
    }
  }

  withAnyKotlinPlugin {
    kotlin.jvmCompilerOptions {
      jvmTarget = JvmTarget.fromTarget(target)
    }
  }

  withAnyAndroidPlugin {
    android.compileOptions {
      sourceCompatibility = JavaVersion.toVersion(target)
      targetCompatibility = JavaVersion.toVersion(target)
    }
  }
}

fun Project.KotlinConvention() {
  withAnyKotlinPlugin {
    kotlin {
      // optIn at the source set level because of IDE quirks
      sourceSets.configureEach {
        languageSettings {
          optIn("kotlin.ExperimentalStdlibApi")
          optIn("kotlin.ExperimentalMultiplatform")
          optIn("kotlin.time.ExperimentalTime")
          optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
          optIn("dev.efemoney.lexiko.engine.api.InternalEngineApi")
        }
      }
      compilerOptions {
        verbose = true
        progressiveMode = true
        freeCompilerArgs./*append*/addAll(
          "-Xannotations-in-metadata", // KT-75736
          "-Xconsistent-data-class-copy-visibility", // KT-11914

          "-XXLanguage:+PackagePrivateFileClassesWithAllPrivateMembers", // KT-10884
        )
      }
      jvmCompilerOptions {
        jvmDefault = JvmDefaultMode.NO_COMPATIBILITY // https://kotlinlang.org/docs/whatsnew22.html#kotlin-jvm
        javaParameters = true
        moduleName = inferModuleName() // avoid conflicts when the .kotlin_module files are included in jars/aars
        freeCompilerArgs./*append*/addAll(
          "-Xjsr305=strict",
          "-Xassertions=jvm", // https://publicobject.com/2019/11/18/kotlins-assert-is-not-like-javas-assert/
          "-Xemit-jvm-type-annotations", // useful for static analysis tools or annotation processors.
        )
      }
    }
  }
}

fun Project.AndroidConvention() {
  withAnyAndroidPlugin {
    android {
      compileSdk = 36
      defaultConfig.minSdk = 28
      testCoverage.jacocoVersion = libs.versions.jacoco.get()
      compileOptions.isCoreLibraryDesugaringEnabled = true
      project.dependencies { "coreLibraryDesugaring"(libs.android.tools.desugar) }
      buildTypes.named("debug") { matchingFallbacks.add("release") }
    }
    androidComponents {
      finalizeDsl {
        when (it.namespace) {
          null -> it.namespace = inferAndroidNamespace()
          inferAndroidNamespace() -> logger.error("$project should infer android namespace instead")
        }
      }
    }
  }
  withAndroidApplicationPlugin {
    android.defaultConfig.targetSdk = 36
  }
  withAndroidLibraryPlugin {
    androidComponents {
      beforeVariants(selector().withBuildType("debug")) { it.enable = false } // disable library debug variants
      beforeVariants { it.androidTest.enable = false } // disable android tests for all library modules
    }
  }
  withAndroidMultiplatformTargetPlugin {
    kotlin {
      android {
        compileSdk = 36
        minSdk = 28
        testCoverage.jacocoVersion = libs.versions.jacoco.get()
        enableCoreLibraryDesugaring = true
        project.dependencies { "coreLibraryDesugaring"(libs.android.tools.desugar) }
        localDependencySelection { selectBuildTypeFrom = listOf("release") }
      }
    }
    androidComponents {
      finalizeDsl {
        when (it.namespace) {
          null -> it.namespace = inferAndroidNamespace()
          inferAndroidNamespace() -> logger.lifecycle("$project, should infer namespace instead")
        }
      }
    }
  }
}

fun Project.IosConvention() {
  withMultiplatformPlugin {
    kotlin {
      swiftExport {
        moduleName = "Lexiko" + inferModuleName().capitalized()
        flattenPackage = "dev.efemoney.lexiko"
      }
    }
  }
}

fun Project.KspConvention() {
  withPlugin("com.google.devtools.ksp") {
  }
}

fun Project.MetroConvention() {
  withPlugin("dev.zacsweers.metro") {
    configure<MetroPluginExtension> {
      publicProviderSeverity = DiagnosticSeverity.ERROR
    }
    withPlugin("reporting-base") {
      configure<MetroPluginExtension> {
        val enabled = providers.gradleProperty("lexiko.metro.enableReports")
        val dir = the<ReportingExtension>().baseDirectory.dir("metro").filter { enabled.orNull == "true" }
        reportsDestination = dir
      }
    }
    withAnyAndroidPlugin {
      dependencies {
        "implementation"(projects.libsDi)
      }
    }
    withKotlinJvmOrAndroidPlugins {
      dependencies {
        "implementation"(projects.libsDi)
      }
    }
    withMultiplatformPlugin {
      kotlin {
        sourceSets.commonMain.dependencies {
          implementation(projects.libsDi)
        }
      }
    }
  }
}

fun Project.ComposeConvention() {
  withPlugin("org.jetbrains.kotlin.plugin.compose") {
    configure<ComposeCompilerGradlePluginExtension> {
      includeSourceInformation = true
      stabilityConfigurationFiles.addAll(
        layout.settingsDirectory.file("stability.config")
      )
    }
    withPlugin("reporting-base") {
      configure<ComposeCompilerGradlePluginExtension> {
        val enabled = providers.gradleProperty("lexiko.compose.enableReports")
        val dir = the<ReportingExtension>().baseDirectory.dir("compose").filter { enabled.orNull == "true" }
        reportsDestination = dir
        metricsDestination = dir
      }
    }
    withKotlinJvmOrAndroidPlugins {
      dependencies {
        "implementation"(libs.jetbrains.compose.runtime)
      }
    }
    withMultiplatformPlugin {
      kotlin {
        sourceSets.commonMain.dependencies {
          implementation(libs.jetbrains.compose.runtime)
        }
      }
    }
  }
}

fun Project.LintConvention() {
  withPlugin("com.android.lint") { the<Lint>().configure() }
  withAnyAndroidPlugin { androidComponents.finalizeDsl { it.lint.configure() } }
  withAndroidMultiplatformTargetPlugin { androidComponents.finalizeDsl { it.lint.configure() } }
}

fun Project.SimpleLayoutConvention() {

  withPlugin("java") {
    the<JavaPluginExtension>().sourceSets.configureEach {
      java.srcDirs(simpleName(name, "src"))
      resources.srcDirs(simpleName(name, "resources"))
    }
  }

  withAnyKotlinPlugin {
    kotlin.sourceSets.configureEach {
      kotlin.srcDirs(simpleName(name, "src"))
      resources.srcDirs(simpleName(name, "resources"))
    }
  }

  withAnyAndroidPlugin {
    android.sourceSets.configureEach {
      java.setSrcDirs(emptyList<String>()) // no java sources
      // AS & IntelliJ map (roughly) a source set to a 'module'
      // This is important because in AS, whatever folder the manifest.xml file lives in becomes a source root
      // according to this comment https://issuetracker.google.com/issues/232007221#comment13
      // We keep manifest files in their own unique folders so intellij does not choke on "duplicate source roots"
      manifest.srcFile("$name/AndroidManifest.xml")
      kotlin.srcDirs(simpleName(name, "src"))
      resources.srcDirs(simpleName(name, "resources"))
      res.srcDirs(simpleName(name, "res"))
      assets.srcDirs(simpleName(name, "assets"))
      aidl.srcDirs(simpleName(name, "aidl"))
      renderscript.srcDirs(simpleName(name, "renderscript"))
      baselineProfiles.srcDirs(simpleName(name, "baselineProfiles"))
      jniLibs.srcDirs(simpleName(name, "jniLibs"))
      shaders.srcDirs(simpleName(name, "shaders"))
      mlModels.srcDirs(simpleName(name, "mlModels"))
    }
  }
}

fun Project.inferModuleName() = path.removePrefix(":").split(':').flatMap { it.split('-') }
  .joinToString(separator = "", transform = { it.capitalized() })

fun Project.inferAndroidNamespace() = path.removePrefix(":").split(':').flatMap { it.split('-') }
  .joinToString(prefix = "dev.efemoney.lexiko.", separator = ".")

fun Lint.configure() {

}

fun Configuration.whenDependencyAdded(dependency: ProjectDependency, action: Action<ProjectDependency>) {
  onDependency(action) { it == dependency }
}

fun Configuration.whenDependencyAdded(
  dependency: ProviderConvertible<MinimalExternalModuleDependency>,
  action: Action<ExternalDependency>,
) {
  whenDependencyAdded(dependency.asProvider(), action)
}

fun Configuration.whenDependencyAdded(
  dependency: Provider<MinimalExternalModuleDependency>,
  action: Action<ExternalDependency>,
) {
  onDependency(action) {
    it.module == dependency.get().module
  }
}

inline fun <reified D : Dependency> Configuration.onDependency(action: Action<D>, filter: Spec<D> = Spec { true }) {
  allDependencies
    .withType<D>()
    .matching(filter)
    .configureEach(action)
}

fun simpleName(name: String, suffix: String) = if (name == "main") suffix else "$name-$suffix"

// endregion

tasks {
  /*register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
  }*/
  dependencyUpdates {
    rejectVersionIf {
      "SNAPSHOT" in candidate.version
    }
  }
}

// region DSL

private typealias CommonExtensionT = CommonExtension

private typealias CommonComponentsExtensionT<T> = AndroidComponentsExtension<T, *, *>


inline fun Project.withConfigurations(vararg names: String, noinline action: (Configuration) -> Unit) {
  val set = names.toSet()
  val includes = names.map(String::capitalized)
  configurations
    .named { it in set || includes.any(it::contains) }
    .configureEach(action)
}

inline fun Project.withPlugin(plugin: String, action: Action<AppliedPlugin>) =
  pluginManager.withPlugin(plugin, action)

inline fun Project.withAllPlugins(vararg plugins: String, action: Action<AppliedPlugin>) {
  withPlugin(
    plugins.first(),
    plugins.drop(1).reversed().fold(action) { actions, current ->
      Action {
        withPlugin(current, actions)
      }
    }
  )
}

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
  action: Action<AppliedAndroidPlugin<CommonExtensionT, CommonComponentsExtensionT<CommonExtensionT>>>,
) {
  withAnyPlugin("com.android.application", "com.android.library") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

inline fun Project.withAndroidLibraryPlugin(
  action: Action<AppliedAndroidPlugin<LibraryExtension, LibraryAndroidComponentsExtension>>,
) {
  withPlugin("com.android.library") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

inline fun Project.withAndroidApplicationPlugin(
  action: Action<AppliedAndroidPlugin<ApplicationExtension, ApplicationAndroidComponentsExtension>>,
) {
  withPlugin("com.android.application") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

inline fun Project.withAndroidMultiplatformTargetPlugin(
  action: Action<AppliedAndroidMultiplatformTargetPlugin>,
) {
  withMultiplatformPlugin {
    withPlugin("com.android.kotlin.multiplatform.library") {
      action.execute(AppliedAndroidMultiplatformTargetPlugin(this))
    }
  }
}

inline fun Project.withMultiplatformPlugin(action: Action<AppliedKotlinPlugin<KotlinMultiplatformExtension>>) {
  withPlugin("org.jetbrains.kotlin.multiplatform") {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun Project.withKotlinJvmOrAndroidPlugins(action: Action<AppliedKotlinPlugin<KotlinProjectExtension>>) {
  withAnyPlugin("org.jetbrains.kotlin.jvm", "org.jetbrains.kotlin.android") {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun Project.withKspPlugin(action: Action<AppliedKspPlugin>) {
  withAnyPlugin("com.google.devtools.ksp") {
    action.execute(AppliedKspPlugin(this))
  }
}

class AppliedAndroidPlugin<T : CommonExtensionT, U : CommonComponentsExtensionT<T>>(applied: AppliedPlugin) :
  AppliedPlugin by applied {

  inline val Project.android get() = extensions.getByName("android") as T

  inline val Project.androidComponents get() = extensions.getByName("androidComponents") as U

  inline fun Project.android(action: Action<T>) = extensions.configure("android", action)

  inline fun Project.androidComponents(action: Action<U>) = extensions.configure("androidComponents", action)
}

class AppliedAndroidMultiplatformTargetPlugin(applied: AppliedPlugin) :
  AppliedKotlinPlugin<KotlinMultiplatformExtension>(applied) {

  inline val KotlinMultiplatformExtension.android
    get() = this.extensions.getByName("android") as KotlinMultiplatformAndroidLibraryTarget

  fun KotlinMultiplatformExtension.android(configure: Action<KotlinMultiplatformAndroidLibraryTarget>): Unit =
    extensions.configure("android", configure)

  inline val Project.androidComponents
    get() = extensions.getByName("androidComponents") as KotlinMultiplatformAndroidComponentsExtension

  inline fun Project.androidComponents(configure: Action<KotlinMultiplatformAndroidComponentsExtension>): Unit =
    extensions.configure("androidComponents", configure)
}

open class AppliedKotlinPlugin<T : KotlinProjectExtension>(applied: AppliedPlugin) : AppliedPlugin by applied {

  inline val Project.kotlin get() = extensions.getByName("kotlin") as T

  inline fun Project.kotlin(action: Action<T>) = extensions.configure("kotlin", action)

  inline fun KotlinProjectExtension.compilerOptions(action: Action<KotlinCommonCompilerOptions>) {
    if (this is KotlinJvmProjectExtension) action.execute(compilerOptions)
    if (this is KotlinAndroidProjectExtension) action.execute(compilerOptions)
    if (this is KotlinMultiplatformExtension) {
      targets.configureEach {
        if (this is HasConfigurableKotlinCompilerOptions<*>) {
          action.execute(compilerOptions)
        }
      }
    }
  }

  inline fun KotlinProjectExtension.jvmCompilerOptions(action: Action<KotlinJvmCompilerOptions>) {
    compilerOptions {
      if (this is KotlinJvmCompilerOptions) action.execute(this)
    }
  }

  inline fun Project.kspConfigurations(crossinline action: (String) -> Unit) {
    when (val kotlin = kotlin) {
      is KotlinSingleTargetExtension<*> -> action("ksp")
      is KotlinMultiplatformExtension -> kotlin.targets
        .matching { it.platformType != KotlinPlatformType.common }
        .configureEach {
          action("ksp" + targetName.run { if (this == "main") "" else this }.capitalized())
        }
    }
  }

  inline fun KotlinMultiplatformExtension.nativeTargets(
    family: Family? = null,
    architecture: Architecture? = null,
    action: Action<KotlinNativeTarget>,
  ) {
    targets.withType<KotlinNativeTarget>()
      .matching {
        (family == null || family == it.konanTarget.family) &&
          (architecture == null || architecture == it.konanTarget.architecture)
      }
      .configureEach(action)
  }

  inline fun KotlinMultiplatformExtension.iosTargets(action: Action<KotlinNativeTarget>) {
    nativeTargets(Family.IOS, action = action)
  }

  inline fun KotlinMultiplatformExtension.swiftExport(action: Action<SwiftExportExtension>) {
    if (extensions.findByName("swiftExport") != null) {
      configure<SwiftExportExtension> {
        action.execute(this)
      }
    }
  }
}

class AppliedKspPlugin(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {

  inline val Project.ksp
    get() = (this as ExtensionAware).extensions.getByName("ksp") as KspExtension

  fun Project.ksp(configure: Action<KspExtension>): Unit =
    (this as ExtensionAware).extensions.configure("ksp", configure)
}

// endregion
