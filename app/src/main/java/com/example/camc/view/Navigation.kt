package com.example.camc.view

import com.example.camc.view.gps_screen.GpsScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.camc.Screen
import com.example.camc.model.room.ReadingsDatabase
import com.example.camc.view.all_sensors.*
import com.example.camc.view.gps_screen.GpsViewModel
import com.example.camc.view.gps_screen.GpsViewModelFactory
import com.example.camc.view.main_screen.MainScreen
import com.example.camc.view.main_screen.MainViewModel
import com.example.camc.view.main_screen.MainViewModelFactory
import com.example.camc.view.selections.SelectionScreen

@Composable
fun Navigation(navController: NavHostController, database: ReadingsDatabase) {
    val mainViewModel = viewModel<MainViewModel>(
        factory = MainViewModelFactory(database.accelerationDao, database.gyroDao, database.magnetDao, database.locationDao)
    )
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(mainViewModel, navController)
        }
        composable(route = Screen.SelectionScreen.route) {
            SelectionScreen(mainViewModel, navController)
        }
        composable(route = Screen.GpsScreen.route){
            val gpsViewModel = viewModel<GpsViewModel>(
                factory = GpsViewModelFactory(database.locationDao)
            )
            GpsScreen(gpsViewModel, navController)
        }
        composable(route = Screen.AllSensorsScreen.route) {
            val allViewModel = viewModel<AllSensorsViewModel>(
                factory = AllSensorsViewModelFactory(database.accelerationDao, database.gyroDao, database.magnetDao, database.locationDao)
            )
            AllSensorsScreen(allViewModel, navController)
        }
    }
}
