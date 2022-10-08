package dev.efemoney.lexiko.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavDirections

const val NO_ID: Int = -1

sealed class CustomDirection : NavDirections {
  override val actionId = NO_ID
  override val arguments = bundleOf()
}

data class PopBackStack(val popUpTo: Int = NO_ID, val popUpToInclusive: Boolean = false) : CustomDirection()

data class DeepLink(val deepLink: String) : CustomDirection()

data class ComposeScreen(val screenName: String) : CustomDirection()
