package dev.efemoney.lexiko.app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.efemoney.lexiko.app.di.AppGraph
import dev.efemoney.lexiko.di.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject

internal class LexikoApp : Application() {
  lateinit var graph: AppGraph
  override fun onCreate() {
    super.onCreate()
    graph = AppGraph()
  }
}

@ContributesIntoSet(AppScope::class)
internal class LexikoActivity @Inject constructor() : ComponentActivity() {
  private inline val app get() = applicationContext as LexikoApp
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { LexikoEntryPoint(app.graph) }
  }
}
