@file:Suppress("UnstableApiUsage")

pluginManager.withMultiplatformPlugin {
  kotlin.android()

  pluginManager.withAnyAndroidPlugin {
    android
      .sourceSets["main"]
      .manifest
      .srcFile("src/androidMain/AndroidManifest.xml")
  }
}
