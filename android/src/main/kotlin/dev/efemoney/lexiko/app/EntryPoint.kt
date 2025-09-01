package dev.efemoney.lexiko.app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.efemoney.lexiko.app.di.AppGraph
import dev.efemoney.lexiko.ui.LexikoTheme

internal class LexikoApp : Application() {
  lateinit var component: AppGraph
  override fun onCreate() {
    super.onCreate()
    component = creGra
  }
}

@Inject
internal class LexicoActivity : ComponentActivity() {
  private inline val app get() = applicationContext as LexikoApp
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val graph =
    setContent {
      LexikoTheme {

      }
    }
  }
}
