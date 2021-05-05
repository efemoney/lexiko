plugins {
  `kotlin-dsl-base`
  `java-gradle-plugin`
}

sourceSets.main.get().java.srcDir("src")

gradlePlugin.plugins.create("plugin") {
  id = "plugin-lexiko-settings"
  implementationClass = "LexikoSettingsPlugin"
}

dependencies {
  api(platform("org.jetbrains.kotlin:kotlin-bom:1.5.0"))
  api("org.jetbrains.kotlin:kotlin-noarg:1.5.0")
  api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")

  api("com.android.tools.build:gradle:7.0.0-alpha15")
  api("com.github.ben-manes:gradle-versions-plugin:0.38.0")
  api("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.5.0-1.0.0-alpha09")
}
