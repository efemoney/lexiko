package dev.efemoney.lexiko.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import dev.efemoney.lexiko.app.internal.retainedComponent
import dev.efemoney.lexiko.app.ui.LexikoApp
import dev.efemoney.lexiko.app.ui.LexikoTheme
import coil.compose.LocalImageLoader as CoilLocalImageLoader
import com.google.accompanist.coil.LocalImageLoader as AccompanistLocalImageLoader

class LexikoActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setDecorFitsSystemWindows(window, false)
    getInsetsController(window, contentView)?.run {
      isAppearanceLightNavigationBars = true
      isAppearanceLightStatusBars = isAppearanceLightNavigationBars && true
    }

    setContent {
      val imageLoader = retainedComponent().imageLoader

      CompositionLocalProvider(
        CoilLocalImageLoader provides imageLoader,
        AccompanistLocalImageLoader provides imageLoader,
        content = {
          LexikoTheme { LexikoApp() }
        }
      )
    }
  }
}

private inline val Activity.contentView get() = window.decorView.findViewById<View>(android.R.id.content)
