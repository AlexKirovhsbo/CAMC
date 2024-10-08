package com.example.camc.view.main_screen
import android.location.LocationManager
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.GyroReading
import com.example.camc.model.room.entities.LocationReading
import com.example.camc.model.room.entities.MagnetReading

data class StaySafeState (
    val sampleRateAccel: Int = 2,

    val isRecording: Boolean = false,
    val showBottomModal: Boolean = false,

    //Accel
    val currentReadingsAccel: List<AccelerationReading> = listOf(
        AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)),
    val singleReadingAccel: AccelerationReading = AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    //GPS
    val sampleRateGpsMs: Float = 15f,
    val meterSelectionGps: Float = 1f,
    val currentReadingsGPS: List<LocationReading> = listOf(),
    val singleReadingGPS: LocationReading = LocationReading(timestampMillis = 0, long = 0.0, lat = 0.0, altitude = 0.0, velocity = 0.0F, bearing = 0.0F),
    val providerGps: String = LocationManager.GPS_PROVIDER,
    val transportationMode: String = "unknown",
)
