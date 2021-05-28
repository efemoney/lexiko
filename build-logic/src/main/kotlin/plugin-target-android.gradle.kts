@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

pluginManager.withPlugin("kotlin-multiplatform") {
  the<KotlinMultiplatformExtension>().android()

  pluginManager.withAnyAndroidPlugin {
    android
      .sourceSets["main"]
      .manifest
      .srcFile("src/androidMain/AndroidManifest.xml")
  }
}
