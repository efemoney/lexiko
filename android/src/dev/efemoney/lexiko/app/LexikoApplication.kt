package dev.efemoney.lexiko.app

import dev.efemoney.lexiko.LexikoComponent
import android.app.Application as AndroidApplication

class LexikoApplication : Application, AndroidApplication() {

  lateinit var component: LexikoComponent

  override fun onCreate() {
    super.onCreate()
    component = component()
  }
}
