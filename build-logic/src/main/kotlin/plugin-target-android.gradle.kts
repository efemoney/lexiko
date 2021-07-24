@file:Suppress("UnstableApiUsage")

pluginManager.withMultiplatformPlugin {
  pluginManager.withAnyAndroidPlugin {
    kotlin.android()
    android
      .sourceSets["main"]
      .manifest
      .srcFile("src/androidMain/AndroidManifest.xml")
  }
}
