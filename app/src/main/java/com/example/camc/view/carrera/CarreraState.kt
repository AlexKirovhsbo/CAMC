package com.example.camc.view.carrera
import android.location.LocationManager
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.GyroReading
import com.example.camc.model.room.entities.LocationReading

data class CarreraState (
    val isRecording: Boolean = false,
    val showBottomModal: Boolean = false,

    //Accel
    val sampleRateAccel: Int = 2,
    val currentReadingsAccel: List<AccelerationReading> = listOf(
        AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)),
    val singleReadingAccel: AccelerationReading = AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    //Gyro
    val sampleRateGyro: Int = 2,
    val currentReadingsGyro: List<GyroReading> = listOf(
        GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)
    ),
    val singleReadingGyro: GyroReading = GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    val transportationMode: String = "unknown",
)
