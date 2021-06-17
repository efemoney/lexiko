@file:Suppress("ClassName", "UnstableApiUsage", "unused")

object Versions {
  const val kotlin = "1.5.20-RC"
  const val coroutines = "1.5.0"
  const val serialization = "1.2.1"
  const val coil = "1.2.2"
  const val accompanist = "0.12.0"
  const val ktor = "1.6.0"
  const val moshi = "1.12.0"
  const val moshix = "0.11.2"
  const val okio = "3.0.0-alpha.6"
  const val okhttp = "5.0.0-alpha.2"
  const val retrofit = "2.9.0"
  const val dagger = "2.37"
  const val glide = "4.12.0"
  const val material = "1.3.0"
  const val timber = "4.7.1"

  object androidx {
    const val core = "1.6.0-rc01"
    const val compose = "1.0.0-beta09"
    const val activity = "1.3.0-beta02"
    const val appcompat = "1.4.0-alpha02"
    const val datastore = "1.0.0-alpha07"
    const val constraintLayout = "2.0.4"
    const val coordinatorLayout = "1.1.0"
    const val fragment = "1.4.0-alpha03"
    const val lifecycle = "2.3.0"
    const val navigation = "2.4.0-alpha03"
    const val preference = "1.1.1"
    const val recyclerview = "1.2.0"
  }
}

object Deps {
  const val junit = "junit:junit:4.13.2"
  const val uuid = "com.benasher44:uuid:0.3.0"
  const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
  const val jsr250 = "javax.annotation:jsr250-api:1.0"

  object kotlin {
    const val bom = "org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"

    object stdlib : Dep("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}") {
      const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
      const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    }
  }

  object kotlinx {
    const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.2.1"

    object immutable : Dep("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.4") {
      const val jvm = "org.jetbrains.kotlinx:kotlinx-collections-immutable-jvm:0.3.4"
    }

    object coroutines {
      const val bom = "org.jetbrains.kotlinx:kotlinx-coroutines-bom:${Versions.coroutines}"
      const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
      const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
      const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
      const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
    }

    object serialization {
      const val bom = "org.jetbrains.kotlinx:kotlinx-serialization-bom:${Versions.serialization}"
      const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}"
    }
  }

  object ktor {
    const val bom = "io.ktor:ktor-bom:${Versions.ktor}"
    const val auth = "io.ktor:ktor-auth:${Versions.ktor}"
    const val websockets = "io.ktor:ktor-websockets:${Versions.ktor}"
    const val locations = "io.ktor:ktor-locations:${Versions.ktor}"

    object server : Dep("io.ktor:ktor-server-core:${Versions.ktor}") {
      const val netty = "io.ktor:ktor-server-netty:${Versions.ktor}"
      const val moshi = "com.hypercubetools:ktor-moshi-server:2.1.0"
    }

    object client : Dep("io.ktor:ktor-client-core:${Versions.ktor}") {
      const val okHttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
      const val moshi = "com.hypercubetools:ktor-moshi-client:2.1.0"
    }
  }

  object androidx {
    const val core = "androidx.core:core-ktx:${Versions.androidx.core}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.androidx.appcompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidx.constraintLayout}"
    const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${Versions.androidx.coordinatorLayout}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.androidx.fragment}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.androidx.fragment}"
    const val preference = "androidx.preference:preference-ktx:${Versions.androidx.preference}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.androidx.recyclerview}"


    object activity : Dep("androidx.activity:activity-ktx:${Versions.androidx.activity}") {
      const val compose = "androidx.activity:activity-compose:${Versions.androidx.activity}"
    }

    object compose {
      const val animation = "androidx.compose.animation:animation:${Versions.androidx.compose}"
      const val compiler = "androidx.compose.compiler:compiler:${Versions.androidx.compose}"
      const val foundation = "androidx.compose.foundation:foundation:${Versions.androidx.compose}"
      const val runtime = "androidx.compose.runtime:runtime:${Versions.androidx.compose}"

      object ui : Dep("androidx.compose.ui:ui:${Versions.androidx.compose}") {
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.androidx.compose}"
      }

      object material : Dep("androidx.compose.material:material:${Versions.androidx.compose}") {
        const val icons = "androidx.compose.material:material-icons-core:${Versions.androidx.compose}"
        const val moarIcons = "androidx.compose.material:material-icons-extended:${Versions.androidx.compose}"
      }
    }

    object navigation {
      const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.androidx.navigation}"
      const val compose = "androidx.navigation:navigation-compose:${Versions.androidx.navigation}"
      const val runtime = "androidx.navigation:navigation-runtime-ktx:${Versions.androidx.navigation}"
      const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.androidx.navigation}"
    }

    object lifecycle {
      const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidx.lifecycle}"
      const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidx.lifecycle}"
      const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.androidx.lifecycle}"
    }
  }

  object accompanist {
    const val coil = "com.google.accompanist:accompanist-coil:${Versions.accompanist}"
    const val glide = "com.google.accompanist:accompanist-glide:${Versions.accompanist}"
    const val pager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"
    const val insets = "com.google.accompanist:accompanist-insets:${Versions.accompanist}"
    const val insetsUi = "com.google.accompanist:accompanist-insets-ui:${Versions.accompanist}"
    const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
  }

  object coil : Dep("io.coil-kt:coil:${Versions.coil}")

  object dagger : Dep("com.google.dagger:dagger:${Versions.dagger}") {
    const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
  }

  object glide : Dep("com.github.bumptech.glide:glide:${Versions.glide}") {
    const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val okHttp = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    const val recyclerView = "com.github.bumptech.glide:recyclerview-integration:${Versions.glide}"
  }

  object okio : Dep("com.squareup.okio:okio:${Versions.okio}") {
    const val multiplatform = "com.squareup.okio:okio-multiplatform:${Versions.okio}"
  }

  object okHttp : Dep("com.squareup.okhttp3:okhttp:${Versions.okhttp}") {
    const val bom = "com.squareup.okhttp3:okhttp-bom:${Versions.okhttp}"
    const val logging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val mockwebserver = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
  }

  object material {
    const val android = "com.google.android.material:material:${Versions.material}"
  }

  object moshi : Dep("com.squareup.moshi:moshi:${Versions.moshi}") {
    const val adapters = "com.squareup.moshi:moshi-adapters:${Versions.moshi}"
    const val lazyAdapters = "com.serjltt.moshi:moshi-lazy-adapters:2.2"
    const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
  }

  object moshix {
    const val ksp = "dev.zacsweers.moshix:moshi-ksp:${Versions.moshix}"
    const val adapters = "dev.zacsweers.moshix:moshi-adapters:${Versions.moshix}"

    object sealed {
      const val codegen = "dev.zacsweers.moshix:moshi-sealed-codegen:${Versions.moshix}"
      const val runtime = "dev.zacsweers.moshix:moshi-sealed-runtime:${Versions.moshix}"
    }
  }

  object retrofit : Dep("com.squareup.retrofit2:retrofit:${Versions.retrofit}") {
    object converter {
      const val moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    }
  }
}

open class Dep(private val module: String) : CharSequence by module {
  override fun toString() = module
}
