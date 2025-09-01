plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform)
  alias(libs.plugins.metro)
}

kotlin {
  android { namespace = "dev.efemoney.lexiko.di" }
  jvm()
  iosArm64()

  sourceSets {
    commonMain.dependencies {
    }
    androidMain.dependencies {
    }
  }
}
