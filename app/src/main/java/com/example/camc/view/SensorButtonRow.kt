package com.example.camc.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.camc.R

@Composable
fun SensorButtonRow(clicked: Boolean,
                    readingCount: Int,
                    onStartClicked: ()->Unit,
                    onStopClicked: ()->Unit,
                    onDeleteClicked: ()->Unit,
                    onSettingsClicked: ()->Unit,
                    threshold: Int = 20,
                    isClickable: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 30.dp
            )
    ) {
        Button(
            onClick = {
                if(isClickable) {
                    if (!clicked) {
                        onStartClicked()
                    } else {
                        onStopClicked()
                    }
                }
            },
            colors =
            if (!clicked) {
                ButtonDefaults.buttonColors(containerColor = if(isClickable) {Color.Green} else Color.LightGray)
            } else ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            if (!clicked) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_play_circle_filled_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Record Data")
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pause_circle_filled_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Stop Recording")
            }
        }


        if (readingCount > threshold) {
            Button(onClick = {
                onDeleteClicked()
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }


        Button(onClick = {
            onSettingsClicked()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = null
            )
            //Spacer(modifier = Modifier.width(5.dp))
            //Text("Settings")
        }
    }
}