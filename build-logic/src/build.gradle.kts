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
  api(platform("org.jetbrains.kotlin:kotlin-bom:1.5.0-M2"))
  api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0-M2")
  api("com.android.tools.build:gradle:7.0.0-alpha14")
  api("com.github.ben-manes:gradle-versions-plugin:0.38.0")
}
