@file:Suppress("FunctionName")

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

internal fun Project.AllTheBoms() {
  configurations
    .matching { it.name == "implementation" }
    .configureEach {
      val implementation = this

      pluginManager.withAnyKotlinPlugin {
        dependencies {
          implementation(platform(Deps.ktor.bom))
          implementation(platform(Deps.kotlin.bom))
          implementation(platform(Deps.kotlinx.coroutines.bom))
        }
      }

      pluginManager.withAnyAndroidPlugin {
        dependencies {
          implementation(platform(Deps.okHttp.bom))
        }
      }
    }
}

internal fun Project.TestsConvention() {
  tasks.withType<Test>().configureEach {
    useJUnit()
    testLogging.showStandardStreams = true
  }
}

internal fun Project.JavaConvention() {
  pluginManager.withPlugin("java") {
    configure<JavaPluginExtension> {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11
    }
  }
}
