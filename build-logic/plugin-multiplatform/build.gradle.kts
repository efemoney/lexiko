plugins {
  `kotlin-dsl-base`
  `java-gradle-plugin`
}

sourceSets.main.get().java.srcDir("src")

dependencies {
  implementation(project(":src"))
  implementation(project(":plugin-android"))
}

gradlePlugin {
  plugins {
    create("mppPlugin") {
      id = "plugin-multiplatform"
      implementationClass = "KotlinMultiplatformPlugin"
    }

    listOf("ios", "android", "jvm").forEach {
      create("${it}Plugin") {
        id = "plugin-$it-target"
        implementationClass = "${it.capitalize()}TargetPlugin"
      }
    }
  }
}
