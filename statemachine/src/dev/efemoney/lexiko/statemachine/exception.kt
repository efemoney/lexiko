package dev.efemoney.lexiko.statemachine

class ActionException(message: String?, cause: Throwable?) : Throwable(message, cause) {
  constructor(message: String?) : this(message, null)
  constructor(cause: Throwable?) : this(cause?.toString(), cause)
}
