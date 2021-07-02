package dev.efemoney.lexiko.engine

import kotlin.jvm.JvmInline

interface Player {
  val id: PlayerId
}

@JvmInline
value class PlayerId(val platformUid: String)
