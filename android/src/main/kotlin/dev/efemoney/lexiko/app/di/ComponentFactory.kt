package dev.efemoney.lexiko.app.di

import android.app.Activity
import android.content.Intent
import androidx.core.app.AppComponentFactory
import dev.efemoney.lexiko.app.LexicoActivity
import dev.efemoney.lexiko.app.LexikoApp

@Suppress("unused")
internal class ComponentFactory : AppComponentFactory() {

  private lateinit var app: LexikoApp

  override fun instantiateApplicationCompat(cl: ClassLoader, className: String) =
    super.instantiateApplicationCompat(cl, className).also { app = it as LexikoApp }

  override fun instantiateActivityCompat(cl: ClassLoader, className: String, intent: Intent?): Activity {
    return when (className) {
      LexicoActivity::class.qualifiedName -> /*app.component.activity()*/TODO()
      else -> super.instantiateActivityCompat(cl, className, intent)
    }
  }
}
