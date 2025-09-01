@file:Suppress("unused")

package dev.efemoney.lexiko.app.di

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import dev.efemoney.lexiko.app.LexikoActivity
import dev.efemoney.lexiko.app.LexikoApp
import dev.efemoney.lexiko.di.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.asContribution

@ContributesTo(AppScope::class)
private interface ComponentAccessors {
  val lexikoActivity: LexikoActivity
}

internal class ComponentFactory : AppComponentFactory() {

  private lateinit var app: LexikoApp

  override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application {
    val app = super.instantiateApplicationCompat(cl, className)
    this.app = app as LexikoApp
    return app
  }

  override fun instantiateActivityCompat(cl: ClassLoader, className: String, intent: Intent?): Activity {
    return when {
      "LexikoActivity" in className -> app.graph.asContribution<ComponentAccessors>().lexikoActivity
      else -> super.instantiateActivityCompat(cl, className, intent)
    }
  }
}
