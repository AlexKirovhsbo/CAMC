package com.example.camc.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class AccelerationReadingInfo(
    val timestampMillis: Long,
    val xAxis: Float,
    val yAxis: Float,
    val zAxis: Float,
    val transportationMode: String?,
    val sensor: String
)
