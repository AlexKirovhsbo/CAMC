package com.example.camc.view

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

fun createPath(xPoints: List<Float>, yPoints: List<Float>): Path {
    val path = Path()
    if (xPoints.isNotEmpty() && yPoints.isNotEmpty()) {
        path.moveTo(xPoints[0], yPoints[0])
        for (i in 1 until xPoints.size) {
            path.lineTo(xPoints[i], yPoints[i])
        }
    }
    return path
}

fun createText(text: String): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic
            )
        ) {
            append(text)
        }
    }
}