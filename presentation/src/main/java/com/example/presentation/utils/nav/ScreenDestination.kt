package com.example.presentation.utils.nav

sealed class ScreenDestinations(val route: String) {

    data object Splash: ScreenDestinations("splash")
    data object Login: ScreenDestinations("login")
    data object Welcome: ScreenDestinations("welcome")

    data object Home : ScreenDestinations("home")

}