package com.example.camc.view.all_sensors

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
fun AllSensorsScreen(viewModel: AllSensorsViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
                        viewModel.onReceiveNewAccelReading(event.values)
                    }
                }

                locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationListener = LocationListener { p0 ->
                    viewModel.onReceiveNewGpsReading(p0.longitude, p0.latitude, p0.speed)
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

    DisposableEffect(key1 = state.sampleRateAccel) {
        sensorManager?.unregisterListener(sensorEventListener)
        sensorManager?.registerListener(
            sensorEventListener,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            state.sampleRateAccel
        )
        onDispose { }
    }

    DisposableEffect(key1 = state.sampleRateGpsMs, key2 = state.meterSelectionGps, key3 = state.providerGps) {
        locationListener?.let { locationManager?.removeUpdates(it) }
        locationListener?.let {
            locationManager!!.requestLocationUpdates(
                state.providerGps,
                state.sampleRateGpsMs.toLong(),
                state.meterSelectionGps,
                it
            )
        }

        onDispose { }
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
        Button(onClick = {
            coroutineScope.launch {
                viewModel.exportAccelerationReadingsToCsv(ctx)
            }
        }) {
            Text("Export für Accelerometer")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            coroutineScope.launch {
                viewModel.exportLocationReadingsToCsv(ctx)
            }
        }) {
            Text("Export für GPS")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            coroutineScope.launch {
                viewModel.exportMergedReadingsToCsv(ctx)
            }
        }) {
            Text("Export für GPS und Accel")
        }
    }


    AllSensorsSettingsModal(
        state = state,
        viewModel = viewModel,

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllSensorsSettingsModal(
    state: AllSensorsState,
    viewModel: AllSensorsViewModel,

) {
    var sliderPositionAccel by remember { mutableStateOf(state.sampleRateAccel + 0.0f) }
    var sliderPositionGps by remember { mutableStateOf(state.sampleRateGpsMs + 0.0f) }
    val transportationModes = listOf("Stehen", "Gehen", "Laufen")
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

                Text(text = getMsRateDescr(state.sampleRateGpsMs.toInt()))
                Slider(
                    value = sliderPositionGps,
                    onValueChange = {
                        sliderPositionGps = it
                        viewModel.setSampleRateGps(it)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 2,
                    valueRange = 0f..15f
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
