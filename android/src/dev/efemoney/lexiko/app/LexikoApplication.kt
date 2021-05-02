package dev.efemoney.lexiko.app

import android.app.Application
import dev.efemoney.lexiko.DaggerCoreComponent
import dev.efemoney.lexiko.app.internal.AppComponent
import dev.efemoney.lexiko.app.internal.DaggerAppComponent

class LexikoApplication : Application() {

  internal lateinit var component: AppComponent

  override fun onCreate() {
    super.onCreate()
    component = DaggerAppComponent.factory().create(this, DaggerCoreComponent.create())
  }
}
