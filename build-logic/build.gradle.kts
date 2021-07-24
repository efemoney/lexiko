import org.gradle.plugin.management.internal.autoapply.AutoAppliedGradleEnterprisePlugin as E

plugins {
  `kotlin-dsl`
}

kotlin.sourceSets.configureEach {
  languageSettings.apply {
    useExperimentalAnnotation("kotlin.Experimental")
    useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
  }
}

dependencies {
  api(platform(kotlin("bom", "1.5.30-M1")))
  api(kotlin("noarg", "1.5.30-M1"))
  api(kotlin("gradle-plugin", "1.5.30-M1"))
  api("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.5.21-1.0.0-beta05")

  api("com.android.tools.build:gradle:7.1.0-alpha04")
  api("com.github.ben-manes:gradle-versions-plugin:0.39.0")

  compileOnly("${E.GROUP}:${E.NAME}:${E.VERSION}")
}
