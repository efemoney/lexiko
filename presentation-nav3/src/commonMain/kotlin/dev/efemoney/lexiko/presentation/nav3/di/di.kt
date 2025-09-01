package dev.efemoney.lexiko.presentation.nav3.di

import dev.efemoney.lexiko.di.ForegroundScope
import dev.efemoney.lexiko.presentation.nav3.NavRegistry
import dev.zacsweers.metro.ContributesTo

@ContributesTo(ForegroundScope::class)
interface NavigationAccessors {
  val registry: NavRegistry
}
