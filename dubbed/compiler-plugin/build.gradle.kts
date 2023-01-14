plugins {
  kotlin("jvm")
}

dependencies {
  compileOnly(libs.kotlin.compiler.embeddable)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinpoet)

  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlin.reflect)
  testImplementation(libs.kotlin.compiler.embeddable)
  testImplementation(libs.kotlin.compileTesting)
//  implementation("com.google.auto.service:auto-service-annotations:1.0.1")
//  ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")

}
