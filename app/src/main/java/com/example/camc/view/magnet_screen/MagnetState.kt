package com.example.camc.view.magnet_screen

import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.MagnetReading

data class MagnetState(
    /* STATE RELEVANT FIELDS FOR ACCELERATION SCREEN */
    val representationMethod: Int = 2,
    val sampleRate: Int = 2,
    val currentReadings: List<MagnetReading> = listOf(
        MagnetReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)
    ),
    val isRecording: Boolean = false,
    val showBottomModal: Boolean = false,
    val singleReading: MagnetReading = MagnetReading(timestampMillis = 0, xAxis = 0f, yAxis = 0f, zAxis = 0f)
)
