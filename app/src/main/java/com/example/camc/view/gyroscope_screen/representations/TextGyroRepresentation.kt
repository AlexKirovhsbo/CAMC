package com.example.camc.view.acceleration_screen.representations

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.camc.model.room.entities.GyroReading

@Composable
fun ReadingGyroTextRepr(readings: List<GyroReading>, showAmount: Int, safetyPadding: Int) {
    LazyColumn {
        items(1) {
            if (readings.size > (showAmount+safetyPadding)) {
                readings.subList(
                    readings.size - showAmount,
                    readings.size
                ).forEach {
                    Text(
                        "(${it.xAxis},  ${it.yAxis}, ${it.zAxis})",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}