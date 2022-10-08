plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  implementation(projects.libs.di.scopes)
  implementation(libs.anvil.compiler)
  implementation(libs.anvil.compiler.api)
  implementation(libs.anvil.compiler.utils)
  implementation(libs.kotlinpoet)
}

