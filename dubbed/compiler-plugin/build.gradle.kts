plugins {
  kotlin("jvm")
}

dependencies {
  compileOnly(Deps.kotlin.compiler.embeddable)
  implementation(Deps.kotlin.stdlib.jdk8)
  implementation(Deps.kotlinpoet)

  testImplementation(Deps.kotlin.test)
  testImplementation(Deps.kotlin.reflect)
  testImplementation(Deps.kotlin.compiler.embeddable)
  testImplementation(Deps.kotlin.compileTesting)
//  implementation("com.google.auto.service:auto-service-annotations:1.0.1")
//  ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")

}
