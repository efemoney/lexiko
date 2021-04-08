plugins {
  id("plugin-simple-layout")
  kotlin("jvm")
  application
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

kotlin.target.compilations.configureEach {
  kotlinOptions.jvmTarget = "11"
}

dependencies {
  implementation(Deps.kotlin15.bom)
  implementation(Deps.kotlin15.stdlib.jdk8)
}
