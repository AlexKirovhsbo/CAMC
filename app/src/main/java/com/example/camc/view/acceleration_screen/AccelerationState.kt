package com.example.camc.view.acceleration_screen

import android.hardware.SensorManager
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.GyroReading
import com.example.camc.model.room.entities.MagnetReading

data class AccelerationState(
    /* STATE RELEVANT FIELDS FOR ACCELERATION SCREEN */
    val representationMethod: Int = 2,
    val sampleRate: Int = 2,
    val currentReadings: List<AccelerationReading> = listOf(
        AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)),
    val isRecording: Boolean = false,
    val showBottomModal: Boolean = false,
    val singleReading: AccelerationReading = AccelerationReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    // Warum das hier rein muss versteh ich nicht :)
    val singleReadinggyro: GyroReading = GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),
    val currentReadingsgyro: List<GyroReading> = listOf(
        GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)
    ),
    val currentReadingsmag: List<MagnetReading> = listOf(
        MagnetReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)),
    val singleReadingmag: MagnetReading = MagnetReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f),


    )
