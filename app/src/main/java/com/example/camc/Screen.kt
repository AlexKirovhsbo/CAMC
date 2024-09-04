package com.example.camc

sealed class Screen(val route: String) {
    object MainScreen : Screen("mainscreen")
    object GpsScreen : Screen("gps")
    object AllSensorsScreen : Screen("allSensors")
    object SelectionScreen : Screen("selection")
}
