import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

pluginManager.withPlugin("kotlin-multiplatform") {
  the<KotlinMultiplatformExtension>().jvm()
}
