import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

pluginManager.withPlugin("kotlin-multiplatform") {
  val projectName = project.name

  the<KotlinMultiplatformExtension>().ios {
    binaries.framework {
      baseName = projectName
    }
  }
}
