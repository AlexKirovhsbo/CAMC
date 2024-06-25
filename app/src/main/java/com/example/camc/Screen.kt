package com.example.camc

sealed class Screen(val route: String) {
    object MainScreen : Screen("mainscreen")
    object GpsScreen : Screen("gps")
    object AccelScreen : Screen("accel")
    object GyroScreen : Screen("gyro")
    object MagScreen : Screen("mag")
    object AllSensorsScreen : Screen("allSensors")
    object CarreraScreen : Screen("carrera")
}