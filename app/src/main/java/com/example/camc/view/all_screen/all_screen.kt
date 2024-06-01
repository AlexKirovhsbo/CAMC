package com.example.camc.view.all_screen

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.example.camc.model.SelectionItem
import com.example.camc.view.SensorButtonRow
import com.example.camc.view.LifeCycleHookWrapper
import com.example.camc.view.SelectorRow
import com.example.camc.view.acceleration_screen.AccelerationState
import com.example.camc.view.acceleration_screen.representations.ReadingAccelTextRepr
import com.example.camc.view.acceleration_screen.representations.ReadingGyroChartRepr
import com.example.camc.view.acceleration_screen.representations.ReadingGyroTextRepr
import com.example.camc.view.getSampleRateDescr
import com.example.camc.view.gyroscope_screen.representations.ReadingAccelChartRepr
import com.example.camc.view.gyroscope_screen.GyroState
import com.example.camc.view.magnet_screen.representations.ReadingMagnetChartRepr
import com.example.camc.view.magnet_screen.representations.ReadingMagnetTextRepr

@Composable
fun AllScreen(viewModel: allViewModel) {
    val state by viewModel.state.collectAsState()
    val ctx = LocalContext.current


    var sensorManager by remember { mutableStateOf<SensorManager?>(null) }
    var sensorEventListener by remember { mutableStateOf<SensorEventListener?>(null) }
    val selection = SelectionItem.litFromLabels("Text", "Chart", "Mixed")

    LifeCycleHookWrapper(
        onEvent = { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                sensorManager!!.unregisterListener(sensorEventListener)
            } else if (event == Lifecycle.Event.ON_CREATE) {
                sensorManager =
                    ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

                sensorEventListener = object : SensorEventListener {
                    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                    }

                    override fun onSensorChanged(event: SensorEvent) {
                        viewModel.onReceiveNewReading(event.values)
                    }
                }

                sensorManager!!.registerListener(
                    sensorEventListener,
                    sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    state.sampleRate
                )
                sensorManager!!.registerListener(
                    sensorEventListener,
                    sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    state.sampleRate
                )
            }
        },
        attachToDipose = { sensorManager?.unregisterListener(sensorEventListener) }
    )

    DisposableEffect(key1 = state.sampleRate) {
        sensorManager?.unregisterListener(sensorEventListener)
        sensorManager!!.registerListener(
            sensorEventListener,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            state.sampleRate
        )


        onDispose { }
    }
    DisposableEffect(key1 = state.sampleRate) {
        sensorManager?.unregisterListener(sensorEventListener)
        sensorManager!!.registerListener(
            sensorEventListener,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            state.sampleRate
        )
        onDispose { }
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        SensorButtonRow(
            clicked = state.isRecording,
            readingCount = state.currentReadings.size,
            onStartClicked = { viewModel.startRecording() },
            onStopClicked = { viewModel.stopRecording() },
            onDeleteClicked = { viewModel.nukeReadings() },
            onSettingsClicked = { viewModel.toggleBottomSheetOpenedTarget() })

        Spacer(modifier = Modifier.height(80.dp))

        if (!state.isRecording && state.currentReadings.size < 20) {
            Text("Accelerometer", fontSize = 32.sp)
            Text(
                "X: ${state.singleReading.xAxis}\nY: ${state.singleReading.yAxis}\nZ: ${state.singleReading.zAxis}",
                fontSize = 20.sp
            )
        } else {
            for (item in selection) {
                if (state.representationMethod == item.associatedValue) {
                    Text(item.label, fontSize = 32.sp)
                }
            }
            when (state.representationMethod) {
                0 -> {
                    ReadingAccelTextRepr(
                        readings = state.currentReadings,
                        showAmount = 20,
                        safetyPadding = 2
                    )
                }

                1 -> {
                    ReadingAccelChartRepr(
                        readings = state.currentReadings,
                        showAmount = 20,
                        safetyPadding = 2
                    )
                }

                2 -> {
                    ReadingAccelChartRepr(
                        readings = state.currentReadings,
                        showAmount = 20,
                        safetyPadding = 2
                    )

                    ReadingAccelTextRepr(
                        readings = state.currentReadings,
                        showAmount = 10,
                        safetyPadding = 2
                    )
                }
            }
        }
    }

    AccelerationSettingsModal(
        state = AllState(),
        viewModel = viewModel,
        selectedValue = state.representationMethod,
        selection = selection
    ) {
        viewModel.setRepresentationMethod(it.associatedValue)
    }

    Text("Gyroskope", fontSize = 32.sp)
    Column ( modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(100.dp))
    SensorButtonRow(
        clicked = state.isRecording,
        readingCount = state.currentReadings.size,
        onStartClicked = { viewModel.startRecording() },
        onStopClicked = { viewModel.stopRecording() },
        onDeleteClicked = { viewModel.nukeReadings() },
        onSettingsClicked = { viewModel.toggleBottomSheetOpenedTarget() })
        Spacer(modifier = Modifier.height(120.dp))
    if (!state.isRecording && state.currentReadings.size < 20) {
        Text("Gyroskope", fontSize = 32.sp)

        Text(
            "X: ${state.singleReading.xAxis}\nY: ${state.singleReading.yAxis}\nZ: ${state.singleReading.zAxis}",
            fontSize = 20.sp
        )
    } else {
        for (item in selection) {
            if (state.representationMethod == item.associatedValue) {
                Text(item.label, fontSize = 32.sp)
            }
        }
        when (state.representationMethod) {
            0 -> {
                ReadingGyroTextRepr(
                    readings = state.currentReadingsgyro,
                    showAmount = 20,
                    safetyPadding = 2
                )
            }

            1 -> {
                ReadingGyroChartRepr(
                    readings = state.currentReadingsgyro,
                    showAmount = 20,
                    safetyPadding = 2
                )
            }

            2 -> {
                ReadingGyroChartRepr(
                    readings = state.currentReadingsgyro,
                    showAmount = 20,
                    safetyPadding = 2
                )

                ReadingGyroTextRepr(
                    readings = state.currentReadingsgyro,
                    showAmount = 10,
                    safetyPadding = 2
                )
            }
        }
    }
    }


    GyroSettingsModal(
    state = AllState(),
    viewModel = viewModel,
    selectedValue = state.representationMethod,
    selection = selection
    ) {
        viewModel.setRepresentationMethod(it.associatedValue)
    }

    Text("Magnetometer", fontSize = 32.sp)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(400.dp))


        if (!state.isRecording && state.currentReadings.size < 20) {
            Text("Magnetometer", fontSize = 32.sp)
            Text(
                "X: ${state.singleReading.xAxis}\nY: ${state.singleReading.yAxis}\nZ: ${state.singleReading.zAxis}",
                fontSize = 20.sp
            )
        } else {
            for(item in selection) {
                if(state.representationMethod == item.associatedValue) {
                    Text(item.label, fontSize = 32.sp)
                }
            }
            when (state.representationMethod) {
                0 -> {
                    ReadingMagnetTextRepr(readings = state.currentReadingsmag,
                        showAmount = 20,
                        safetyPadding = 2)
                }
                1 -> {
                    ReadingMagnetChartRepr(readings = state.currentReadingsmag,
                        showAmount = 20,
                        safetyPadding = 2)
                }
                2 -> {
                    ReadingMagnetChartRepr(readings = state.currentReadingsmag,
                        showAmount = 20,
                        safetyPadding = 2)

                    ReadingMagnetTextRepr(readings = state.currentReadingsmag,
                        showAmount = 10,
                        safetyPadding = 2)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccelerationSettingsModal(
    state: AllState,
    viewModel: allViewModel,
    selectedValue: Int,
    selection: List<SelectionItem>,
    onRepresentationChanged: (SelectionItem)->Unit,
) {
    var sliderPosition by remember { mutableStateOf(state.sampleRate + 0.0f) }

    if (state.showBottomModal) {
        ModalBottomSheet(onDismissRequest = { viewModel.setBottomSheetOpenedTarget(false) }) {

            Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                Text(text = getSampleRateDescr(state.sampleRate))
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        viewModel.setSampleRate(it.toInt())
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 2,
                    valueRange = 0f..3f
                )

                SelectorRow(
                    items = selection,
                    selectedValue = selectedValue,
                ) { selectedItem ->
                    onRepresentationChanged(selectedItem)
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GyroSettingsModal(
    state: AllState,
    viewModel: allViewModel,
    selectedValue: Int,
    selection: List<SelectionItem>,
    onRepresentationChanged: (SelectionItem)->Unit
) {
    var sliderPosition by remember { mutableStateOf(state.sampleRate + 0.0f) }

    if (state.showBottomModal) {
        ModalBottomSheet(onDismissRequest = { viewModel.setBottomSheetOpenedTarget(false) }) {
            Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                Text(text = getSampleRateDescr(state.sampleRate))
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        viewModel.setSampleRate(it.toInt())
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 2,
                    valueRange = 0f..3f
                )

                SelectorRow(
                    items = selection,
                    selectedValue = selectedValue,
                ) { selectedItem ->
                    onRepresentationChanged(selectedItem)
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagnetSettingsModal(
    state: AllState,
    viewModel: MagnetViewModel,
    selectedValue: Int,
    selection: List<SelectionItem>,
    onRepresentationChanged: (SelectionItem)->Unit,
) {
    var sliderPosition by remember { mutableStateOf(state.sampleRate + 0.0f) }

    if (state.showBottomModal) {
        ModalBottomSheet(onDismissRequest = { viewModel.setBottomSheetOpenedTarget(false) }) {

            Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                Text(text = getSampleRateDescr(state.sampleRate))
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        viewModel.setSampleRate(it.toInt())
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 2,
                    valueRange = 0f..3f
                )

                SelectorRow(
                    items = selection,
                    selectedValue = selectedValue,
                ) { selectedItem ->
                    onRepresentationChanged(selectedItem)
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}