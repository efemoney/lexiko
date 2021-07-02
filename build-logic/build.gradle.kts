plugins {
  `kotlin-dsl`
}

dependencies {
  api(platform(kotlin("bom", "1.5.20")))
  api(kotlin("noarg", "1.5.20"))
  api(kotlin("gradle-plugin", "1.5.20"))
  api("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.5.20-1.0.0-beta04")

  api("com.android.tools.build:gradle:7.1.0-alpha02")
  api("com.github.ben-manes:gradle-versions-plugin:0.38.0")
}
