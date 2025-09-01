package dev.efemoney.lexiko.engine.api

@RequiresOptIn("This API is internal to the engine", RequiresOptIn.Level.ERROR)
@Suppress("ExperimentalAnnotationRetention")
@Retention(AnnotationRetention.BINARY)
@Target(
  AnnotationTarget.CLASS,
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.TYPEALIAS,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
)
@MustBeDocumented
internal annotation class InternalEngineApi
