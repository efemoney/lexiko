@file:Suppress("UnstableApiUsage")

pluginManager.withAnyAndroidPlugin {
  android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.compose
  }
}
