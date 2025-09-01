package dev.efemoney.lexiko.presentation.nav3.di

import dev.efemoney.lexiko.di.AppScope
import dev.efemoney.lexiko.presentation.nav3.NavRegistry
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
internal interface NavigationComponent {
  val registry: NavRegistry
}
