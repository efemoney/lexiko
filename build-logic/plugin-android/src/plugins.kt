@file:Suppress("UnstableApiUsage", "NOTHING_TO_INLINE")

import com.android.build.api.dsl.ApplicationBuildFeatures
import com.android.build.api.dsl.BaseFlavor
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.DynamicFeatureBuildFeatures
import com.android.build.api.dsl.LibraryBuildFeatures
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) = with(target) {
    pluginManager.withAnyAndroidPlugin {

      android {

        compileSdkVersion(30)

        defaultConfig {
          minSdkVersion(21)
          targetSdkVersion(30)
          multiDexEnabled = true
          vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
          sourceCompatibility = JavaVersion.VERSION_1_8
          targetCompatibility = JavaVersion.VERSION_1_8

          // https://developer.android.com/studio/releases/gradle-plugin#j8-library-desugaring
          isCoreLibraryDesugaringEnabled = true
          target.dependencies { "coreLibraryDesugaring"("com.android.tools:desugar_jdk_libs:1.1.1") }
        }

        buildFeatures.disableAll()
      }
    }
  }

  private inline fun BuildFeatures.disableAll() {
    // Some other plugin might have set these flags so only change them if they have default value of 'null'

    if (aidl == null) aidl = false
    if (compose == null) compose = false
    if (buildConfig == null) buildConfig = false
    if (prefab == null) prefab = false
    if (renderScript == null) renderScript = false
    if (shaders == null) shaders = false
    if (resValues == null) resValues = false
    if (viewBinding == null) viewBinding = false

    when (this) {
      is ApplicationBuildFeatures -> {
        if (dataBinding != null) dataBinding = false
        if (mlModelBinding != null) mlModelBinding = false
      }
      is LibraryBuildFeatures -> {
        if (androidResources != null) androidResources = false
        if (dataBinding != null) dataBinding = false
        if (mlModelBinding != null) mlModelBinding = false
        if (prefabPublishing != null) prefabPublishing = false
      }
      is DynamicFeatureBuildFeatures -> {
        if (dataBinding != null) dataBinding = false
        if (mlModelBinding != null) mlModelBinding = false
      }
    }
  }
}

class AndroidLibraryPlugin : Plugin<Project> {

  override fun apply(target: Project) = with(target) {
    pluginManager.apply(LibraryPlugin::class)
    pluginManager.apply(AndroidConventionPlugin::class)
  }
}

class AndroidAppPlugin : Plugin<Project> {

  override fun apply(target: Project) = with(target) {
    pluginManager.apply(AppPlugin::class)
    pluginManager.apply(AndroidConventionPlugin::class)
  }
}

class AndroidComposePlugin : Plugin<Project> {

  override fun apply(target: Project) = with(target) {

    pluginManager.withAnyAndroidPlugin {
      android {
        buildFeatures.compose = true
        composeOptions {
          kotlinCompilerExtensionVersion = Versions.compose
        }
      }
    }
  }
}
