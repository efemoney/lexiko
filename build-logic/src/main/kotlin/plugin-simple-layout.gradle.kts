@file:Suppress("UnstableApiUsage", "NOTHING_TO_INLINE", "UNREACHABLE_CODE")

import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension

pluginManager.withPlugin("java") {
  the<JavaPluginExtension>().sourceSets.configureEach {
    java.setSrcDirs(listOf(simpleName(name, "src")))
    resources.setSrcDirs(listOf(simpleName(name, "resources")))
  }
}

pluginManager.withAnyKotlinPlugin {
  when (kotlin) {
    is KotlinSingleTargetExtension -> kotlin.sourceSets.configureEach {
      kotlin.setSrcDirs(listOf(simpleName(name, "src")))
      resources.setSrcDirs(listOf(simpleName(name, "resources")))
    }
    // is KotlinMultiplatformExtension -> kotlin.sourceSets.configureEach {
    //   if (name.startsWith("android")) return@configureEach // skip android sourcesets for now

    //   val suffixIndex = name.indexOfLast(Char::isUpperCase)
    //   val suffix = name.substring(suffixIndex).lowercase().takeIf { it != "main" }
    //   val targetName = name.substring(0, suffixIndex)
    //   kotlin.setSrcDirs(listOf("$targetName/${suffix ?: "src"}"))
    //   resources.setSrcDirs(listOf("$targetName/${suffix?.let { "$it-resources " } ?: "resources"}"))
    // }
  }
}

pluginManager.withAnyAndroidPlugin {
  android.sourceSets.configureEach {
    java.setSrcDirs(listOf(simpleName(name, "java-src")))
    kotlin.setSrcDirs(listOf(simpleName(name, "src")))
    resources.setSrcDirs(listOf(simpleName(name, "resources")))
    manifest.srcFile(simpleName(name, "AndroidManifest.xml"))
    res.setSrcDirs(listOf(simpleName(name, "res")))
    assets.setSrcDirs(listOf(simpleName(name, "assets")))
    aidl.setSrcDirs(listOf(simpleName(name, "aidl")))
    renderscript.setSrcDirs(listOf(simpleName(name, "renderscript")))
    jniLibs.setSrcDirs(listOf(simpleName(name, "jniLibs")))
    shaders.setSrcDirs(listOf(simpleName(name, "shaders")))
    mlModels.setSrcDirs(listOf(simpleName(name, "mlModels")))
  }
}

inline fun simpleName(name: String, suffix: String) = buildString {
  val namePart = if (name.endsWith("main", true)) name.dropLast(4) else name
  append(namePart)
  if (namePart.isNotEmpty()) append('-')
  append(suffix)
}
