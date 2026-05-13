package dev.efemoney.lexiko.app.di

import dev.efemoney.lexiko.di.ForegroundScope
import dev.efemoney.lexiko.presentation.nav3.NavRegistry
import dev.zacsweers.metro.ContributesTo

@ContributesTo(ForegroundScope::class)
internal interface NavRegistryAccessor {
  val registry: NavRegistry
}
