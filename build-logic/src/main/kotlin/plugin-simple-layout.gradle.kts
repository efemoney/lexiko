@file:Suppress("UnstableApiUsage", "NOTHING_TO_INLINE")

pluginManager.withMultiplatformPlugin {

  kotlin.sourceSets
    .matching { listOf(/*"common",*/ "ios", "jvm").any(it.name::startsWith) }
    .configureEach {
      kotlin.setSrcDirs(listOf(simpleName(name, "src")))
      resources.setSrcDirs(listOf(simpleName(name, "resources")))
    }

  kotlin.sourceSets
    .matching { it.name.startsWith("android") }
    .configureEach {
      val other = name.removePrefix("android").decapitalize()
        .let { if (it == "main") "" else "-$it" }

      val actualName = "android$other"

      kotlin.setSrcDirs(listOf(simpleName(actualName, "src")))
      resources.setSrcDirs(listOf(simpleName(actualName, "resources")))
    }
}

pluginManager.withAnyPlugin("java", "java-library") {
  the<JavaPluginExtension>().sourceSets.configureEach {
    java.setSrcDirs(listOf(simpleName(name, "src")))
    resources.setSrcDirs(listOf(simpleName(name, "resources")))
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
