package dev.efemoney.lexiko.navigation

interface Direction

interface Navigator {
  fun navigate(direction: Direction)
}

fun Navigator.goBack() = popBackStack()

fun Navigator.popBackStack(
  popUpTo: Int = NO_ID,
  popUpToInclusive: Boolean = false
) = navigate(PopBackStack(popUpTo, popUpToInclusive))
