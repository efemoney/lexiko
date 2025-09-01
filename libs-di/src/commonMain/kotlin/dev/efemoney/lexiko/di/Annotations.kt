package dev.efemoney.lexiko.di

/**
 * Indicates that the annotated API is read-only & not intended for public implementation.
 */
@Target(AnnotationTarget.CLASS)
@RequiresOptIn
annotation class ReadOnlyGraphApi
