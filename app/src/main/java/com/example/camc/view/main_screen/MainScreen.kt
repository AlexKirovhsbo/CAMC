@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.camc.view.main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.telephony.SmsManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.example.camc.view.LifeCycleHookWrapper
import com.example.camc.view.permission_screen.CoarseLocationPermissionTextProvider
import com.example.camc.view.permission_screen.FineLocationPermissionTextProvider
import com.example.camc.view.permission_screen.PermissionDialog
import kotlinx.coroutines.delay


@SuppressLint("MissingPermission")
@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val latestClassification by viewModel.latestClassification.collectAsState()
    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    var sensorManager by remember { mutableStateOf<SensorManager?>(null) }
    var sensorEventListener by remember { mutableStateOf<SensorEventListener?>(null) }
    var locationManager by remember { mutableStateOf<LocationManager?>(null) }
    var locationListener by remember { mutableStateOf<LocationListener?>(null) }

    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )
    LifeCycleHookWrapper(
        onEvent = { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                sensorManager?.unregisterListener(sensorEventListener)
            } else if (event == Lifecycle.Event.ON_CREATE) {
                sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

                sensorEventListener = object : SensorEventListener {
                    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

                    override fun onSensorChanged(event: SensorEvent) {
                        viewModel.onReceiveNewAccelReading(event.values)
                    }
                }

                locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationListener = LocationListener { p0 ->
                    viewModel.onReceiveNewGpsReading(p0.longitude, p0.latitude, p0.speed, p0.bearing)
                }
                locationListener?.let {
                    locationManager!!.requestLocationUpdates(
                        state.providerGps,
                        state.sampleRateGpsMs.toLong(),
                        state.meterSelectionGps,
                        it
                    )
                }

                sensorManager?.let { sm ->
                    sensorEventListener?.let { sel ->
                        sm.registerListener(
                            sel,
                            sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            state.sampleRateAccel,
                        )
                    }
                }
            }
        },
        attachToDipose = { sensorManager?.unregisterListener(sensorEventListener) }
    )

    LaunchedEffect(key1 = state.sampleRateAccel) {
        while (true) {
            // Unregister and register sensor listener
            sensorManager?.unregisterListener(sensorEventListener)
            sensorManager?.registerListener(
                sensorEventListener,
                sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                state.sampleRateAccel
            )

            // Delay for 1 second before next execution
            delay(1000L) // 1000 milliseconds = 1 second
        }
    }

    LaunchedEffect(key1 = state.sampleRateGpsMs, key2 = state.meterSelectionGps, key3 = state.providerGps) {
        while (true) {
            // Remove and request location updates
            locationListener?.let { locationManager?.removeUpdates(it) }
            locationListener?.let {
                locationManager?.requestLocationUpdates(
                    state.providerGps,
                    state.sampleRateGpsMs.toLong(),
                    state.meterSelectionGps,
                    it
                )
            }

            // Delay for 1 second before next execution
            delay(1000L) // 1000 milliseconds = 1 second
        }
    }

    LaunchedEffect(key1 = Unit) {
        multiplePermissionResultLauncher.launch(permissionsToRequest)
    }

    var showInfoDialog by remember { mutableStateOf(false) }

    // Display permission dialogs if needed
    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        CoarseLocationPermissionTextProvider()
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        FineLocationPermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    LocalContext.current as Activity,
                    permission
                ),
                onDismiss = viewModel::dismissDialog,
                onOkClick = {
                    viewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = openAppSettings()
            )
        }
    val context = LocalContext.current

    Scaffold(
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Position the Row at the top right
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(80.dp) // Adjust padding as needed
            ) {
                // Place the Konfiguration Button
                Button(onClick = {
                    navController.navigate("selection") {
                        popUpTo("selection") { inclusive = true } // This will clear the back stack to avoid multiple instances
                    }
                }) {
                    Text("Konfiguration")
                }
                Spacer(Modifier.width(16.dp))

                Spacer(Modifier.width(16.dp)) // Add space between the button and the IconButton

                // Place the IconButton
                IconButton(onClick = { showInfoDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info"
                    )
                }
            }

            // Center the other content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, // Center content vertically
                modifier = Modifier
                    .fillMaxSize() // Ensure Column takes up full size for proper centering
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(200.dp))
                Button(onClick = {
                    viewModel.startStopRecording()
                }) {
                    if (state.isRecording)
                    {
                        Text("Stopp")
                    }
                    else
                    {
                        Text("Start")
                    }
                }
                Text(text = latestClassification)
            }
        }
    }



    // Show InfoDialog if showInfoDialog is true
    if (showInfoDialog) {
        InfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
fun InfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Information")
        },
        text = {
            Text(
                text = "Stay Safe soll dir helfen unterwegs sicherer zu sein; wähle einfach aus, wie du heute unterwegs sein möchtest, und falls wir merken, du bewegst dich anders als geplant fort, schicken wir an deinen Notfallkontakt eine Nachricht. Probier es einfach aus!",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", LocalContext.current.packageName, null)
    )
    LocalContext.current.startActivity(intent)
}
