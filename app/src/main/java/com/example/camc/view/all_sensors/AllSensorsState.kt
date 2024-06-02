package com.example.camc.view.all_sensors
import android.location.LocationManager
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.GyroReading
import com.example.camc.model.room.entities.LocationReading
import com.example.camc.model.room.entities.MagnetReading

data class AllSensorsState (
    val representationMethod: Int = 2,
    val sampleRate: Int = 2,

    val isRecording: Boolean = false,
    val showBottomModal: Boolean = false,
    //Gyro
    val currentReadingsGyro: List<GyroReading> = listOf(
        GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)),
    val singleReadingGyro: GyroReading = GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    //Magnet
    val currentReadingsMag: List<MagnetReading> = listOf(
        MagnetReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)
    ),
    val singleReadingMag: MagnetReading = MagnetReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    //Accel
    val currentReadingsAccel: List<AccelerationReading> = listOf(
        AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)),
    val singleReadingAccel: AccelerationReading = AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    //GPS
    val sampleRateMs: Float = 15f,
    val meterSelection: Float = 1f,
    val currentReadingsGPS: List<LocationReading> = listOf(),
    val singleReadingGPS: LocationReading = LocationReading(timestampMillis = 0, long = 0.0, lat = 0.0, altitude = 0.0),
    val providerGPS: String = LocationManager.GPS_PROVIDER
)
