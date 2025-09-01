package dev.efemoney.lexiko.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalThemeApplied = staticCompositionLocalOf { false }

@Composable
fun LexikoTheme(content: @Composable () -> Unit) {
  if (LocalThemeApplied.current) return content()

  CompositionLocalProvider(
    LocalThemeApplied provides true,
    content = content,
  )
}
