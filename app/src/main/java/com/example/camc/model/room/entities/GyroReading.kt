package com.example.camc.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GyroReading(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val timestampMillis: Long,
    val xAxis: Float,
    val yAxis: Float,
    val zAxis: Float,
    val transportationMode: String? = null,
    val sensor : String = "gyrosensor",
)
