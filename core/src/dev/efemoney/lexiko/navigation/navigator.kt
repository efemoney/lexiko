package dev.efemoney.lexiko.navigation

import androidx.navigation.NavDirections

interface Navigator {
  fun navigate(direction: NavDirections)
}

fun Navigator.goBack() = popBackStack()

fun Navigator.popBackStack(
  popUpTo: Int = NO_ID,
  popUpToInclusive: Boolean = false
) = navigate(PopBackStack(popUpTo, popUpToInclusive))
