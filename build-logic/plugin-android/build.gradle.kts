plugins {
  `kotlin-dsl-base`
  `java-gradle-plugin`
}

sourceSets.main.get().java.srcDir("src")

dependencies {
  implementation(project(":src"))
}

gradlePlugin {
  plugins {

    create("appPlugin") {
      id = "plugin-android-app"
      implementationClass = "AndroidAppPlugin"
    }

    create("libPlugin") {
      id = "plugin-android-library"
      implementationClass = "AndroidLibraryPlugin"
    }

    create("composePlugin") {
      id = "plugin-compose-support"
      implementationClass = "AndroidComposePlugin"
    }
  }
}
