package com.example.camc.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class LocationReadingInfo(
    val timestampMillis: Long,
    val velocity: Float,
    val bearing: Float,
    val transportationMode: String?,
    val sensor: String
)
