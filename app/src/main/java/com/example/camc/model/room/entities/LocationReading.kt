package com.example.camc.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class LocationReading(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestampMillis: Long,
    val lat: Double,
    val long: Double,
    val altitude: Double
)