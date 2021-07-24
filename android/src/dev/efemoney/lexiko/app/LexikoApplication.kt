package dev.efemoney.lexiko.app

import android.app.Application
import dev.efemoney.lexiko.DaggerCoreComponent
import dev.efemoney.lexiko.app.internal.DaggerAppComponent
import dev.efemoney.lexiko.app.internal.SingletonComponent

class LexikoApplication : Application() {

  internal lateinit var component: SingletonComponent

  override fun onCreate() {
    super.onCreate()
    component = DaggerAppComponent.factory().create(this, DaggerCoreComponent.create())
  }
}
