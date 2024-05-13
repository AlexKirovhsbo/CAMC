package com.example.camc.view.magnet_screen.representations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.camc.model.room.entities.MagnetReading
import com.example.camc.view.createPath
import com.example.camc.view.createText

@OptIn(ExperimentalTextApi::class)
@Composable
fun ReadingMagnetChartRepr(readings: List<MagnetReading>, showAmount: Int,
                     safetyPadding: Int, chartHeight: Dp = 100.dp) {
    val maxRange = 50f
    val minRange = -50f
    val yRange = maxRange - minRange
    val textMeasurer = rememberTextMeasurer()

    if(readings.size > showAmount + safetyPadding) {
        Spacer(modifier = Modifier.height(40.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            // Calculate the size of each data point
            val dataPointWidth = size.width / showAmount.toFloat()
            val dataPointHeight = size.height / yRange

            val yLabelPositions = listOf(-50f, 0f, 50f)
            val yLabelStrings = listOf("-50", "0", "50µT")

            // Draw the y-axis labels

            for ((i, yLabel) in yLabelPositions.withIndex()) {
                val yPosition = size.height - (yLabel - minRange) * dataPointHeight

                drawText(
                    textMeasurer = textMeasurer,
                    text = createText(yLabelStrings[i]),
                    topLeft = Offset(4f, yPosition - 8f),
                )
            }
            val xStart = size.width - dataPointWidth * showAmount
            val xPoints = readings.takeLast(showAmount).mapIndexed { index, _ ->
                xStart + index * dataPointWidth
            }

            // Draw lines for x, y, and z axes
            val yXPoints = readings.takeLast(showAmount).map { reading ->
                size.height - (reading.xAxis - minRange) * dataPointHeight
            }
            val yYPoints = readings.takeLast(showAmount).map { reading ->
                size.height - (reading.yAxis - minRange) * dataPointHeight
            }
            val yZPoints = readings.takeLast(showAmount).map { reading ->
                size.height - (reading.zAxis - minRange) * dataPointHeight
            }

            drawPath(
                createPath(xPoints, yXPoints),
                color=Color.Red,
                style=Stroke(width=8f))

            drawPath(
                createPath(xPoints, yYPoints),
                color=Color.Green,
                style=Stroke(width=8f))

            drawPath(
                createPath(xPoints, yZPoints),
                color=Color.Blue,
                style=Stroke(width=8f))
        }
    }
}