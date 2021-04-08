package dev.efemoney.lexiko.app

import dev.efemoney.lexiko.LexikoComponent

interface Application

expect fun Application.component(): LexikoComponent
