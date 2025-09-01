plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  compileOnly(libs.kotlin.compiler.embeddable)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinpoet)

  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlin.reflect)
  testImplementation(libs.kotlin.compiler.embeddable)
  testImplementation(libs.kotlin.compileTesting)
}
