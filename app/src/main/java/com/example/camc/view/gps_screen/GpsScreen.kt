package com.example.camc.view.gps_screen
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.example.camc.R
import com.example.camc.model.SelectionItem
import com.example.camc.view.LifeCycleHookWrapper
import com.example.camc.view.SelectorRow
import com.example.camc.view.SensorButtonRow
import com.example.camc.view.getMsRateDescr
import com.example.camc.view.gps_screen.GpsViewModel
import com.example.camc.view.gps_screen.LocationState
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@Composable
fun GpsScreen(
    viewModel: GpsViewModel,
    navController: NavHostController
) {

    val state by viewModel.state.collectAsState()
    val ctx = LocalContext.current

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

    var locationManager by remember { mutableStateOf<LocationManager?>(null) }
    var locationListener by remember { mutableStateOf<LocationListener?>(null) }
    var mapView = remember { MapView(ctx).apply {
        controller.setZoom(20.0)   // Initial zoom level
        setMultiTouchControls(true)
        setBuiltInZoomControls(true)
    } }
    val controller = remember { mapView.controller as IMapController }
    val selection = SelectionItem.litFromLabels("1m", "10m", "50m")
    var checked by remember { mutableStateOf(false) }

    LifeCycleHookWrapper(
        attachToDipose = {},
        onEvent = { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                locationListener?.let { locationManager?.removeUpdates(it) }
            } else if (event == Lifecycle.Event.ON_CREATE) {
                locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationListener = LocationListener { location ->
                    viewModel.onReceiveNewReading(location.longitude, location.latitude)
                    controller.setCenter(GeoPoint(location.latitude, location.longitude))  // Center map on location
                    mapView.invalidate()  // Ensure the map updates with the new location
                }
                locationListener?.let {
                    locationManager!!.requestLocationUpdates(
                        state.provider,
                        state.sampleRateMs.toLong(),
                        state.meterSelection,
                        it
                    )
                }
            }
        }
    )

    DisposableEffect(key1 = state.sampleRateMs, key2 = state.meterSelection, key3 = state.provider) {
        locationListener?.let { locationManager?.removeUpdates(it) }
        locationListener?.let {
            locationManager!!.requestLocationUpdates(
                state.provider,
                state.sampleRateMs.toLong(),
                state.meterSelection,
                it
            )
        }

        onDispose { }
    }

    SingleReadingMarker(state = state, mapView = mapView)
    if (state.currentReadings.isNotEmpty()) {
        MultiReadingMarker(state = state, mapView = mapView)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        val context = LocalContext.current
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", 0))
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView },
            update = {
                controller.setCenter(GeoPoint(state.singleReading.lat, state.singleReading.long))
                mapView.invalidate()
            }
        )
        Modifier.fillMaxSize()


        Row (
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(
                    Color.Gray.copy(alpha = 0.5f),
                    RoundedCornerShape(10.dp)
                ).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    if(checked) viewModel.setProvider(LocationManager.NETWORK_PROVIDER)
                    else viewModel.setProvider(LocationManager.GPS_PROVIDER)
                }
            )
            Text(text = " Network")
        }



        Button(
            onClick = {
                mapView.controller.animateTo(
                    GeoPoint(
                        state.singleReading.lat,
                        state.singleReading.long
                    ), 20.0, null
                )
                //mapView.invalidate()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_gps_fixed_24),
                contentDescription = null,
            )
        }

    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(65.dp))
        SensorButtonRow(
            clicked = state.isRecording,
            readingCount = state.currentReadings.size,
            onStartClicked = { viewModel.startRecording() },
            onStopClicked = { viewModel.stopRecording() },
            onDeleteClicked = {
                viewModel.nukeReadings()
                mapView.overlays.clear()
                viewModel.setSingleReading(state.singleReading)
            },
            onSettingsClicked = { viewModel.toggleBottomSheetOpenedTarget() },
            threshold = 1
        )

        Text(
            text = "Long: ${state.singleReading.long} \n Lat: ${state.singleReading.lat}",
            modifier = Modifier
                .padding(5.dp)
                .background(
                    Color.Gray.copy(alpha = 0.5f),
                    RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
        )
    }

    GpsSettingsModal(
        state = state,
        viewModel = viewModel,
        selectedValue = state.representationMethod,
        selection = selection,
    ) {
        viewModel.setRepresentationMethod(it.associatedValue)

        viewModel.setMeterSelection(
            when (state.representationMethod) {
                0 -> 1F
                1 -> 10F
                2 -> 50F
                else -> 1F
            }
        )

    }
}

@Composable
fun SingleReadingMarker(state: LocationState, mapView: MapView) {
    val ctx = LocalContext.current

    DisposableEffect(key1 = state.singleReading, key2 = state.triggerUpdate) {
        var position = GeoPoint(state.singleReading.lat, state.singleReading.long)
        mapView.controller.animateTo(position)
        mapView.controller.setZoom(20.0)
        mapView.invalidate()

        val currentLocationMarker = Marker(mapView)
        currentLocationMarker.icon =
            ContextCompat.getDrawable(ctx, R.drawable.baseline_person_pin_24)
        currentLocationMarker.position = position
        currentLocationMarker.title = "Hier bin ich"

        //FIXME: only delete blue marker if we want to keep showing the
        //      last route
        mapView.overlays.clear()
        mapView.overlays.add(currentLocationMarker)
        mapView.invalidate()

        onDispose { }
    }
}

@Composable
fun MultiReadingMarker(state: LocationState, mapView: MapView) {
    val ctx = LocalContext.current
    val dateFormat = SimpleDateFormat("HH:mm")
    dateFormat.timeZone = TimeZone.getTimeZone("Europe/Berlin")

    DisposableEffect(key1 = state.currentReadings) {
        mapView.overlays.clear()
        state.currentReadings.forEach {
            val readingMarker = Marker(mapView)
            readingMarker.icon =
                ContextCompat.getDrawable(ctx, R.drawable.baseline_person_pin_circle_24)
            readingMarker.position = GeoPoint(it.lat, it.long)
            readingMarker.title = "Hier war ich um \n" + dateFormat.format(Date(it.timestampMillis))
            mapView.overlays.add(readingMarker)
            mapView.invalidate()
        }
        onDispose { }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsSettingsModal(
    state: LocationState,
    viewModel: GpsViewModel,
    selectedValue: Int,
    selection: List<SelectionItem>,
    onRepresentationChanged: (SelectionItem) -> Unit,
) {
    var sliderPosition by remember { mutableStateOf(state.sampleRateMs + 0.0f) }
    if (state.showBottomModal) {
        ModalBottomSheet(onDismissRequest = { viewModel.setBottomSheetOpenedTarget(false) }) {

            Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                Text(text = getMsRateDescr(state.sampleRateMs.toInt()))
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        viewModel.setSampleRate(it)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 2,
                    valueRange = 0f..15f
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
