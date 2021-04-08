package dev.efemoney.lexiko.app

import dev.efemoney.lexiko.DaggerLexikoComponent
import dev.efemoney.lexiko.LexikoComponent

actual fun Application.component(): LexikoComponent = DaggerLexikoComponent.factory().create()
