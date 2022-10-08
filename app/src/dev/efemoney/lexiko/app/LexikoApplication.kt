package dev.efemoney.lexiko.app

import android.app.Application
import coil.ImageLoaderFactory
import dev.efemoney.lexiko.DaggerCoreComponent
import dev.efemoney.lexiko.app.internal.DaggerSingletonComponent
import dev.efemoney.lexiko.app.internal.SingletonComponent

class LexikoApplication : Application(), ImageLoaderFactory {

  internal lateinit var component: SingletonComponent

  override fun onCreate() {
    super.onCreate()
    component = DaggerSingletonComponent.factory().create(this, DaggerCoreComponent.create())
  }

  override fun newImageLoader() = component.imageLoader
}
