package com.example.camc.view.gyroscope_screen

import com.example.camc.model.room.entities.GyroReading

data class GyroState(
    /* STATE RELEVANT FIELDS FOR ACCELERATION SCREEN */
    val representationMethod: Int = 2,
    val sampleRate: Int = 2,
    val currentReadings: List<GyroReading> = listOf(
        GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)
    ),
    val isRecording: Boolean = false,
    val showBottomModal: Boolean = false,
    val singleReading: GyroReading = GyroReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)
)