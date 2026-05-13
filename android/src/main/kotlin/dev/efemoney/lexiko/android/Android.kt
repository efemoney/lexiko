package dev.efemoney.lexiko.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.efemoney.lexiko.app.LexikoEntryPoint
import dev.efemoney.lexiko.app.di.AppGraph
import dev.efemoney.lexiko.app.di.ForegroundGraph
import dev.efemoney.lexiko.di.AppScope
import dev.efemoney.lexiko.di.castAs
import dev.zacsweers.metro.Inject

internal class LexikoApp : Application() {
  lateinit var graph: AppScope.Graph
  override fun onCreate() {
    super.onCreate()
    graph = AppGraph()
  }
}

@Inject
internal class LexikoActivity : ComponentActivity() {
  private val appGraph get() = (applicationContext as LexikoApp).graph
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      LexikoEntryPoint {
        appGraph
          .castAs<ForegroundGraph.Factory>()
          .create()
      }
    }
  }
}
