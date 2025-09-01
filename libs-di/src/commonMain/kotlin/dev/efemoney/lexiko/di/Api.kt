package dev.efemoney.lexiko.di

import dev.zacsweers.metro.asContribution

abstract class AppScope private constructor() {
  /**
   * Marker interface representing the dependency graph for the [AppScope].
   *
   * This graph acts as the entry point for retrieving screen-scoped dependencies,
   * typically via [asContribution].
   *
   * This interface is not intended to be implemented; metro provides the concrete implementation.
   *
   * @see AppScope
   */
  @SubclassOptInRequired(ReadOnlyGraphApi::class)
  interface Graph
}

abstract class BackgroundScope private constructor() {
  /**
   * Marker interface representing the dependency graph for the [BackgroundScope].
   *
   * This graph acts as the entry point for retrieving screen-scoped dependencies,
   * typically via [asContribution].
   *
   * This interface is not intended to be implemented; metro provides the concrete implementation.
   *
   * @see BackgroundScope
   */
  @SubclassOptInRequired(ReadOnlyGraphApi::class)
  interface Graph
}

abstract class ForegroundScope private constructor() {
  /**
   * Marker interface representing the dependency graph for the [ForegroundScope].
   *
   * This graph acts as the entry point for retrieving screen-scoped dependencies,
   * typically via [asContribution].
   *
   * This interface is not intended to be implemented; metro provides the concrete implementation.
   *
   * @see ForegroundScope
   */
  @SubclassOptInRequired(ReadOnlyGraphApi::class)
  interface Graph
}

abstract class ScreenScope private constructor() {
  /**
   * Marker interface representing the dependency graph for the [ScreenScope].
   *
   * This graph acts as the entry point for retrieving screen-scoped dependencies,
   * typically via [asContribution].
   *
   * This interface is not intended to be implemented; metro provides the concrete implementation.
   *
   * @see ScreenScope
   */
  @SubclassOptInRequired(ReadOnlyGraphApi::class)
  interface Graph
}
