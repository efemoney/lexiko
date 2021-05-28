plugins {
  `kotlin-dsl`
}

dependencies {
  api(platform("org.jetbrains.kotlin:kotlin-bom:1.5.10"))
  api("org.jetbrains.kotlin:kotlin-noarg:1.5.10")
  api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
  api("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.5.10-1.0.0-beta01")

  api("com.android.tools.build:gradle:7.0.0-beta03")
  api("com.github.ben-manes:gradle-versions-plugin:0.38.0")
}
