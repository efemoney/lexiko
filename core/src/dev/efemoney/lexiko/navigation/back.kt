package dev.efemoney.lexiko.navigation

fun Navigator.goBack() = navigate(PopBackStack(NO_ID, false))
