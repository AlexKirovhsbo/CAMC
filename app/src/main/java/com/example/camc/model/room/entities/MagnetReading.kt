package com.example.camc.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MagnetReading(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val timestampMillis: Long,
    val xAxis: Float,
    val yAxis: Float,
    val zAxis: Float,
)
