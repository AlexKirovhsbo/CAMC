package com.example.camc.view.gps_screen

import android.location.LocationManager
import com.example.camc.model.room.entities.LocationReading


data class LocationState(
    /* STATE RELEVANT FIELDS FOR ACCELERATION SCREEN */
    val representationMethod: Int = 0,
    val sampleRateMs: Float = 15f,
    val meterSelection: Float = 1f,
    val currentReadings: List<LocationReading> = listOf(),
    val isRecording: Boolean = false,
    val singleReading: LocationReading = LocationReading(timestampMillis = 0, long = 0.0, lat = 0.0, altitude = 0.0, velocity = 0.0F, bearing = 0.0F),
    val permissiongiven: Boolean = false,
    val showBottomModal: Boolean = false,
    val triggerUpdate: Boolean = false,
    val provider: String = LocationManager.GPS_PROVIDER
)