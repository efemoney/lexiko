@file:Suppress("UnstableApiUsage")

plugins { `kotlin-dsl` }

java.toolchain
  .languageVersion
  .set(JavaLanguageVersion.of(8))

dependencies {
  api(platform(Deps.kotlin.bom))
  api(Deps.kotlin.gradlePlugin)
  api(Deps.kotlin.noarg.gradlePlugin)
  api(Deps.ksp.gradlePlugin)
  api(Deps.android.gradlePlugin)
}
