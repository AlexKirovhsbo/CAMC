package com.example.camc.view
import android.hardware.SensorManager

/**
 * @method getampleRateFromInt
 *
 * @param i: the sampleRate collected from the slider
 * @return: the sampleRate mapped to SensorManager Enum
 *      TODO: mapping is 1:1 at the moment and redundant, might change later though
 */
fun getSampleRateFromInt(i: Int): Int {
    return when(i) {
        3 -> SensorManager.SENSOR_DELAY_NORMAL  //langsamstes
        2 -> SensorManager.SENSOR_DELAY_UI
        1 -> SensorManager.SENSOR_DELAY_GAME
        0 -> SensorManager.SENSOR_DELAY_FASTEST //schnellstes
        else -> SensorManager.SENSOR_DELAY_NORMAL
    }
}

/**
 * @method getSampleRateDescr
 *
 * @param i: sampleRate in range of SenorManager Enum
 * @return: String-descriptor to show in GUI
 */
fun getSampleRateDescr(i: Int): String {
    return when(i) {
        3 -> "Normal" //langsamstes
        2 -> "UI"
        1 -> "Game"
        0 -> "Fastest" //schnellstes
        else -> "Normal"
    }
}

/**
 * @method getMsRateDescr
 *
 * @param i: msRate in range of SenorManager Enum
 * @return: String-descriptor to show in GUI
 */
fun getMsRateDescr(i: Int): String {
    return when(i) {
        15 -> "15ms" //langsamstes
        10 -> "10ms"
        5 -> "5ms"
        0 -> "0ms" //schnellstes
        else -> "15ms"
    }
}