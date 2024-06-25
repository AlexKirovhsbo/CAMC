package com.example.camc.view.carrera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.example.camc.view.SensorButtonRow
import com.example.camc.view.LifeCycleHookWrapper
import com.example.camc.view.getMsRateDescr
import com.example.camc.view.getSampleRateDescr
import kotlinx.coroutines.launch

@Composable
fun CarreraScreen(viewModel: CarreraViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    if (ActivityCompat.checkSelfPermission(
            ctx,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            ctx,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        navController.navigate("mainscreen")
        return
    }

    var sensorManager by remember { mutableStateOf<SensorManager?>(null) }
    var sensorEventListener by remember { mutableStateOf<SensorEventListener?>(null) }
    var locationManager by remember { mutableStateOf<LocationManager?>(null) }
    var locationListener by remember { mutableStateOf<LocationListener?>(null) }

    LifeCycleHookWrapper(
        onEvent = { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                sensorManager?.unregisterListener(sensorEventListener)
            } else if (event == Lifecycle.Event.ON_CREATE) {
                sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

                sensorEventListener = object : SensorEventListener {
                    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

                    override fun onSensorChanged(event: SensorEvent) {
                        when (event.sensor.type) {
                            Sensor.TYPE_ACCELEROMETER -> {
                                // Handle accelerometer readings
                                viewModel.onReceiveNewAccelReading(event.values)
                            }
                            Sensor.TYPE_GYROSCOPE -> {
                                // Handle gyroscope readings
                                viewModel.onReceiveNewGyroReading(event.values)
                            }
                        }
                    }
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
                sensorManager!!.registerListener(
                    sensorEventListener,
                    sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    state.sampleRateGyro
                )
            }
        },
        attachToDipose = { sensorManager?.unregisterListener(sensorEventListener) }
    )

    DisposableEffect(key1 = state.sampleRateAccel) {
        sensorManager?.unregisterListener(sensorEventListener)
        sensorManager?.registerListener(
            sensorEventListener,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            state.sampleRateAccel
        )
        onDispose { }
    }

    DisposableEffect(key1 = state.sampleRateGyro) {
        sensorManager?.unregisterListener(sensorEventListener)
        sensorManager!!.registerListener(
            sensorEventListener,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            state.sampleRateGyro
        )
        onDispose {  }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        SensorButtonRow(
            clicked = state.isRecording,
            readingCount = state.currentReadingsAccel.size,
            onStartClicked = { viewModel.startRecording() },
            onStopClicked = { viewModel.stopRecording() },
            onDeleteClicked = { viewModel.nukeReadings() },
            onSettingsClicked = { viewModel.toggleBottomSheetOpenedTarget() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Dropdown menu button
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            Button(onClick = { expanded = true }) {
                Text("Export Options")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    coroutineScope.launch {
                        viewModel.exportAccelerationReadingsToCsv(ctx)
                    }
                    expanded = false
                }, text = { Text("Export für Accelerometer") })

                DropdownMenuItem(onClick = {
                    coroutineScope.launch {
                        viewModel.exportGyroReadingsToCsv(ctx)
                    }
                    expanded = false
                }, text = { Text("Export für Gyro") })

                DropdownMenuItem(onClick = {
                    coroutineScope.launch {
                        viewModel.exportMergedReadingsToCsv(ctx)
                    }
                    expanded = false
                }, text = { Text("Export für Gyro und Accel") })
            }
        }
    }

    CarreraSettingsModal(
        state = state,
        viewModel = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarreraSettingsModal(
    state: CarreraState,
    viewModel: CarreraViewModel,
) {
    var sliderPositionAccel by remember { mutableStateOf(state.sampleRateAccel + 0.0f) }
    var sliderPositionGyro by remember { mutableStateOf(state.sampleRateGyro + 0.0f) }
    val transportationModes = listOf("Links", "Geradeaus", "Rechts", "Gemischt")
    var selectedMode by remember { mutableStateOf(transportationModes.first()) }

    if (state.showBottomModal) {
        ModalBottomSheet(onDismissRequest = { viewModel.setBottomSheetOpenedTarget(false) }) {
            Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                //ACCEL dann GPS
                Text(text = getSampleRateDescr(state.sampleRateAccel))
                Slider(
                    value = sliderPositionAccel,
                    onValueChange = {
                        sliderPositionAccel = it
                        viewModel.setSampleRateAccel(it.toInt())
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 2,
                    valueRange = 0f..3f
                )

                Text(text = getMsRateDescr(state.sampleRateGyro.toInt()))
                Slider(
                    value = sliderPositionGyro,
                    onValueChange = {
                        sliderPositionGyro = it
                        viewModel.setSampleRateGyro(it.toInt())
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 2,
                    valueRange = 0f..3f
                )
                Text("Fortbewegungsart wählen:")
                transportationModes.forEach { mode ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedMode == mode),
                            onClick = {
                                selectedMode = mode
                                viewModel.setTransportationMode(mode)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = mode)
                    }
                }
            }
        }
    }
}
