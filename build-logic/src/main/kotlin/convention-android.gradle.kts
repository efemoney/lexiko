@file:Suppress("UnstableApiUsage", "NOTHING_TO_INLINE")

import com.android.build.api.dsl.*

pluginManager.withAnyAndroidPlugin {
  android {
    compileSdk = 30

    defaultConfig {
      minSdk = 21
      when (this) {
        is ApplicationDefaultConfig -> targetSdk = 30
        is LibraryDefaultConfig -> targetSdk = 30
      }
      vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11

      // https://developer.android.com/studio/releases/gradle-plugin#j8-library-desugaring
      isCoreLibraryDesugaringEnabled = true
      dependencies { "coreLibraryDesugaring"("com.android.tools:desugar_jdk_libs:1.1.5") }
    }

    buildFeatures.disableAll()
  }
}

inline fun BuildFeatures.disableAll() {
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
