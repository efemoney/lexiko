@file:Suppress("UnstableApiUsage", "FunctionName")

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension
import org.gradle.plugin.management.internal.autoapply.AutoAppliedGradleEnterprisePlugin

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

gradle.rootProject {
  apply<VersionsPlugin>() // ben-manes
}

gradle.beforeProject {
  AllTheBoms()
  JavaConvention()
  TestsConvention()
  KotlinConvention()
  KaptConvention()
  AndroidConvention()
}

pluginManager.withPlugin(AutoAppliedGradleEnterprisePlugin.ID.id) {
  the<GradleEnterpriseExtension>().buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    buildScanPublished {
      exec { commandLine("open", buildScanUri) }
    }
  }
}
