package dev.efemoney.lexiko.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import dev.efemoney.lexiko.app.ui.LexikoApp

class LexikoActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setDecorFitsSystemWindows(window, false)
    setContent { LexikoApp() }
  }
}
