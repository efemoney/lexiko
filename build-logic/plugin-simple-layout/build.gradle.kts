plugins {
  `kotlin-dsl-base`
  `java-gradle-plugin`
}

sourceSets.main.get().java.srcDir("src")

dependencies {
  implementation(project(":src"))
}

gradlePlugin.plugins.create("plugin") {
  id = "plugin-simple-layout"
  implementationClass = "SimpleLayoutPlugin"
}
