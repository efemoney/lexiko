package dev.efemoney.lexiko.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavDirections

const val NO_ID: Int = -1

actual typealias Direction = NavDirections

sealed class CustomDirection : Direction {
  override fun getActionId() = NO_ID
  override fun getArguments() = bundleOf()
}

data class PopBackStack(val popUpTo: Int = NO_ID, val popUpToInclusive: Boolean = false) : CustomDirection()

data class DeepLink(val deepLink: String) : CustomDirection()

data class ComposeScreen(val screenName: String) : CustomDirection()

actual fun Navigator.goBack() = popBackStack()

fun Navigator.popBackStack(
  popUpTo: Int = NO_ID,
  popUpToInclusive: Boolean = false
) = navigate(PopBackStack(popUpTo, popUpToInclusive))
