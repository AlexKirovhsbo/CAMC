package com.example.camc.view

import com.example.camc.view.gps_screen.GpsScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.camc.Screen
import com.example.camc.model.room.ReadingsDatabase
import com.example.camc.view.acceleration_screen.AccelerationScreen
import com.example.camc.view.acceleration_screen.AccelerationViewModel
import com.example.camc.view.acceleration_screen.AccelerationViewModelFactory
import com.example.camc.view.all_sensors.*
import com.example.camc.view.gps_screen.GpsViewModel
import com.example.camc.view.gps_screen.GpsViewModelFactory
import com.example.camc.view.gyroscope_screen.GyroViewModel
import com.example.camc.view.gyroscope_screen.GyroViewModelFactory
import com.example.camc.view.gyroscope_screen.GyroscopeScreen
import com.example.camc.view.magnet_screen.MagnetScreen
import com.example.camc.view.magnet_screen.MagnetViewModel
import com.example.camc.view.magnet_screen.MagnetViewModelFactory
import com.example.camc.view.main_screen.MainScreen
import com.example.camc.view.main_screen.MainViewModel
import com.example.camc.view.main_screen.MainViewModelFactory

@Composable
fun Navigation(navController: NavHostController, database: ReadingsDatabase) {
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            val mainViewModel = viewModel<MainViewModel>(
                factory = MainViewModelFactory()
            )
            MainScreen(mainViewModel)
        }
        composable(route = Screen.GpsScreen.route){
            val gpsViewModel = viewModel<GpsViewModel>(
                factory = GpsViewModelFactory(database.locationDao)
            )
            GpsScreen(gpsViewModel, navController)
        }
        composable(route = Screen.AccelScreen.route){
            val accelerationViewModel = viewModel<AccelerationViewModel>(
                factory = AccelerationViewModelFactory(database.accelerationDao)
            )
            AccelerationScreen(accelerationViewModel)
        }
        composable(route = Screen.GyroScreen.route){
            val gyroViewModel = viewModel<GyroViewModel>(
                factory = GyroViewModelFactory(database.gyroDao)
            )
            GyroscopeScreen(gyroViewModel)
        }
        composable(route = Screen.MagScreen.route){
            val magnetViewModel = viewModel<MagnetViewModel>(
                factory = MagnetViewModelFactory(database.magnetDao)
            )
            MagnetScreen(magnetViewModel)
        }
        composable(route = Screen.AllSensorsScreen.route) {
            val allViewModel = viewModel<AllSensorsViewModel>(
                factory = AllSensorsViewModelFactory(database.accelerationDao, database.gyroDao, database.magnetDao, database.locationDao)
            )
            AllSensorsScreen(allViewModel, navController)
        }
    }
}
