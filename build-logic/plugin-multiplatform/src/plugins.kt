@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

class KotlinMultiplatformPlugin : Plugin<Project> {

  override fun apply(target: Project) = with(target) {
    pluginManager.apply(KotlinMultiplatformPluginWrapper::class)
  }
}

class AndroidTargetPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    pluginManager.apply(AndroidLibraryPlugin::class)
    pluginManager.withPlugin("kotlin-multiplatform") {
      the<KotlinMultiplatformExtension>().android()
      the<BaseExtension>()
        .sourceSets["main"]
        .manifest
        .srcFile("src/androidMain/AndroidManifest.xml")
    }
  }
}

class IosTargetPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.withPlugin("kotlin-multiplatform") {
      the<KotlinMultiplatformExtension>().ios {
        binaries.framework {
          baseName = target.name
        }
      }
    }
  }
}

class JvmTargetPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.apply(JavaLibraryPlugin::class)
    pluginManager.withPlugin("kotlin-multiplatform") {
      the<KotlinMultiplatformExtension>().jvm()
    }

    configure<JavaPluginConvention> {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }
  }
}

