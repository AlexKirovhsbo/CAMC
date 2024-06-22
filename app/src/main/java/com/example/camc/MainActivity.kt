package com.example.camc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.camc.model.NavItem
import com.example.camc.model.room.ReadingsDatabase
import com.example.camc.ui.theme.Prakt1Theme
import com.example.camc.view.Navigation
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val db by lazy  {
        Room.databaseBuilder(
            applicationContext,
            ReadingsDatabase::class.java,
            "readings.db"
        ).fallbackToDestructiveMigration().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Prakt1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Nav(db = db)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nav(modifier: Modifier = Modifier,
        db: ReadingsDatabase) {
    val navController = rememberNavController()
    val items = listOf(
        NavItem(
            title = "Home",
            selectedIcon = painterResource(R.drawable.baseline_home_24),
            unselectedIcon = painterResource(R.drawable.baseline_home_24),
            route = "mainscreen"
        ),
        /*NavItem(
            title = "Location",
            selectedIcon = painterResource(R.drawable.baseline_place_24),
            unselectedIcon = painterResource(R.drawable.baseline_place_24),
            route = "gps"
        ),*/
        NavItem(
            title = "Alle Sensoren",
            selectedIcon = painterResource(R.drawable.baseline_place_24),
            unselectedIcon = painterResource(R.drawable.baseline_place_24),
            route = "allSensors"
        ),
       NavItem(
            title = "Acceleration",
            selectedIcon = painterResource(R.drawable.baseline_speed_24),
            unselectedIcon = painterResource(R.drawable.baseline_speed_24),
            route = "accel"
        ),
        NavItem(
            title = "Gyroscope",
            selectedIcon = painterResource(R.drawable.baseline_screen_rotation_alt_24),
            unselectedIcon = painterResource(R.drawable.baseline_screen_rotation_alt_24),
            route = "gyro"
        ),
        NavItem(
            title = "Magnetometer",
            selectedIcon = painterResource(R.drawable.baseline_explore_24),
            unselectedIcon = painterResource(R.drawable.baseline_explore_24),
            route = "mag"
        )
        

    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    ModalNavigationDrawer(
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet{
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                               Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            navController.navigate(item.route)
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(painter =
                                if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                }
                                , contentDescription = null)

                        },
                        badge = {
                            item.badgeCount?.let {
                                Text(text = item.badgeCount.toString())
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Gruppe 1")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ){
            Navigation(navController, database = db)
        }
        
    }
}

