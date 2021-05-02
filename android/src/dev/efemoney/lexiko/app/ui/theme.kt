package dev.efemoney.lexiko.app.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.efemoney.lexiko.app.R

@Composable
fun LexikoTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colors = lexikoLightColors,
    typography = lexikoTypography,
    shapes = lexikoShapes,
    content
  )
}

val coolBlue = Color(0xff009df3)
val oceanBlue = Color(0xff037ab9)
val bahamaBlue = Color(0xff0a669b)

val whitish = Color(0xfff8f9fa)
val blackish = Color(0xff212529)

val grayish = Color(0xffadb5bd)

val lexikoLightColors = lightColors(
  primaryVariant = bahamaBlue,
  primary = oceanBlue,
  onPrimary = Color.White,
  surface = Color.White,
  onSurface = blackish,
  background = whitish,
  onBackground = blackish,
)

val lexikoTypography = Typography(
  defaultFontFamily = FontFamily(
    Font(R.font.circe_rounded_regular, FontWeight.Normal),
    Font(R.font.circe_rounded_bold, FontWeight.Bold),
    Font(R.font.circe_rounded_extrabold, FontWeight.ExtraBold),
  ),
  h6 = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
  ),
  body1 = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
  ),
)

val lexikoShapes = Shapes()
