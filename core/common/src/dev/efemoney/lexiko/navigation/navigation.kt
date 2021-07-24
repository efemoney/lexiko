package dev.efemoney.lexiko.navigation

interface Navigator {

  fun navigate(direction: Direction)
}

expect interface Direction

expect fun Navigator.goBack()
