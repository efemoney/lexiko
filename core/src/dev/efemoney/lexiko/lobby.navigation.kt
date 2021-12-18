@file:Suppress("PackageDirectoryMismatch")

package dev.efemoney.lexiko.lobby

import dev.efemoney.lexiko.navigation.ComposeScreen
import dev.efemoney.lexiko.navigation.Navigator

fun Navigator.gotoProfile() = navigate(ComposeScreen("profile"))
