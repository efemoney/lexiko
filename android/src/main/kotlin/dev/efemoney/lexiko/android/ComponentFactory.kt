package dev.efemoney.lexiko.android

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import dev.efemoney.lexiko.android.di.ComponentAccessors
import dev.efemoney.lexiko.di.castAs

internal class ComponentFactory : AppComponentFactory() {

  private lateinit var app: LexikoApp

  override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application {
    return super.instantiateApplicationCompat(cl, className).also { this.app = it as LexikoApp }
  }

  override fun instantiateActivityCompat(cl: ClassLoader, className: String, intent: Intent?): Activity {
    val components = app.graph.castAs<ComponentAccessors>()
    return when {
      "LexikoActivity" in className -> components.lexikoActivity
      else -> super.instantiateActivityCompat(cl, className, intent)
    }
  }
}
