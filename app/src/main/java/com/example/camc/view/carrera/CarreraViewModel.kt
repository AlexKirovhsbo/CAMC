package com.example.camc.view.carrera

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.camc.model.room.dao.AccelerationDao
import com.example.camc.model.room.dao.GyroDao
import com.example.camc.model.room.dao.LocationDao
import com.example.camc.model.room.dao.MagnetDao
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.AccelerationReadingInfo
import com.example.camc.model.room.entities.GyroReading
import com.example.camc.model.room.entities.LocationReading
import com.example.camc.model.room.entities.GyroInfoReading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.FileWriter
import java.util.logging.Logger
import java.io.File
class CarreraViewModel(
    private val accelerationDao: AccelerationDao,
    private val gyroDao: GyroDao,
): ViewModel() {
    private val _readingsAccel = accelerationDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _readingsGyro = gyroDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(CarreraState())
    val state = combine(_state, _readingsAccel, _readingsGyro)
    { screenState, _readingsAccel, _readingsGyro ->
        screenState.copy (
            currentReadingsAccel = _readingsAccel,
            currentReadingsGyro = _readingsGyro,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), CarreraState())
    companion object {
        val logger = Logger.getLogger(CarreraViewModel::class.java.name)
    }

    /* TODO: Generate more methods for state-mutation */
    /**
     * function onRecieveNewReading: sets state's current reading to new inserted reading
     *          conditional: if state is set to 'recording', the new datapoint is written into
     *                       the database
     * @param values: Is the new reading with which the state and database are updated
     *
     * NOTE: As there are 3 parts to the reading, the function should fail if the number differs
     */
    fun onReceiveNewAccelReading(values: FloatArray) {
        assert(values.size >= 3)

        var reading = AccelerationReading(
            timestampMillis = System.currentTimeMillis(),
            xAxis = values[0],
            yAxis = values[1],
            zAxis = values[2],
            transportationMode = _state.value.transportationMode,
        )

        _state.update {
            it.copy(
                singleReadingAccel = reading
            )
        }

        var executed = false
        viewModelScope.launch {
            if (_state.first().isRecording && !executed) {
                accelerationDao.insertReading(reading)
                executed = true
            } else {
                return@launch
            }
        }
    }

    fun onReceiveNewGyroReading(values: FloatArray) {
        assert(values.size >= 3)

        var reading = GyroReading(
            timestampMillis = System.currentTimeMillis(),
            xAxis = values[0],
            yAxis = values[1],
            zAxis = values[2],
            transportationMode = _state.value.transportationMode,
        )

        _state.update {
            it.copy(
                singleReadingGyro = reading
            )
        }

        var executed = false
        viewModelScope.launch {
            if (_state.first().isRecording && !executed) {
                gyroDao.insertReading(reading)
                executed = true
            } else {
                return@launch
            }
        }
    }

    fun setBottomSheetOpenedTarget(target: Boolean) {
        _state.update {
            it.copy(
                showBottomModal = target
            )
        }
    }
    fun toggleBottomSheetOpenedTarget() {
        _state.update {
            it.copy(
                showBottomModal = !it.showBottomModal
            )
        }
    }

    fun setSampleRateGyro(sampleRate: Int) {
        _state.update {
            it.copy(
                sampleRateGyro = sampleRate
            )
        }
    }

    fun setSampleRateAccel(sampleRate: Int) {
        _state.update {
            it.copy(
                sampleRateAccel = sampleRate
            )
        }
    }

    fun startRecording() {
        nukeReadings()
        _state.update {
            it.copy(
                isRecording = true
            )
        }
    }

    fun stopRecording() {
        _state.update {
            it.copy(
                isRecording = false
            )
        }
    }

    fun nukeReadings() {
        viewModelScope.launch {
            accelerationDao.nukeReadings()
            gyroDao.nukeReadings()
        }
    }

    fun setTransportationMode(tpm: String) {
        _state.update {
            it.copy(
                transportationMode = tpm
            )
        }
    }

    suspend fun exportAccelerationReadingsToCsv(context: Context) {
        val readings = accelerationDao.getReadingInfoOrderedByTime().first()
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "acceleration_readings.csv")

        FileWriter(file).use { writer ->
            writer.append("timestampMillis,xAxis,yAxis,zAxis,transportationMode,sensor\n")
            readings.forEach { reading ->
                writer.append("${reading.timestampMillis},${reading.xAxis},${reading.yAxis},${reading.zAxis},${reading.transportationMode ?: ""},${reading.sensor}\n")
            }
        }
    }

    suspend fun exportGyroReadingsToCsv(context: Context) {
        val readings = gyroDao.getReadingInfoOrderedByTime().first()
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "gyro_readings.csv")

        FileWriter(file).use { writer ->
            writer.append("timestampMillis,xAxis,yAxis,zAxis,transportationMode,sensor\n")
            readings.forEach { reading ->
                writer.append("${reading.timestampMillis},${reading.xAxis},${reading.yAxis},${reading.zAxis},${reading.transportationMode ?: ""},${reading.sensor}\n")
            }
        }
    }
    suspend fun exportMergedReadingsToCsv(context: Context) {
        val accelerationReadings = accelerationDao.getReadingInfoOrderedByTime().first()
        val gyroReadings = gyroDao.getReadingInfoOrderedByTime().first()
        val mergedReadings = mergeReadings(accelerationReadings, gyroReadings)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "merged_readings.csv")

        FileWriter(file).use { writer ->
            writer.append("timestampMillis,accel_xAxis,accel_yAxis,accel_zAxis,accel_transportationMode,accel_sensor,gyro_xAxis,gyro_yAxis,gyro_zAxis,gyro_transportationMode,gyro_sensor\n")
            mergedReadings.forEach { reading ->
                writer.append("${reading.timestampMillis},${reading.accel?.xAxis ?: ""},${reading.accel?.yAxis ?: ""},${reading.accel?.zAxis ?: ""},${reading.accel?.transportationMode ?: ""},${reading.accel?.sensor ?: ""},${reading.location?.xAxis ?: ""},${reading.location?.yAxis ?: ""},${reading.location?.zAxis ?: ""},${reading.location?.transportationMode ?: ""},${reading.location?.sensor ?: ""}\n")
            }
        }
    }

    private fun mergeReadings(accelReadings: List<AccelerationReadingInfo>, gyroReadings: List<GyroInfoReading>): List<MergedReading> {
        val mergedList = mutableListOf<MergedReading>()

        var accelIndex = 0
        var locationIndex = 0

        var lastAccel: AccelerationReadingInfo? = null
        var lastLocation: GyroInfoReading? = null

        while (accelIndex < accelReadings.size || locationIndex < gyroReadings.size) {
            val accel = if (accelIndex < accelReadings.size) accelReadings[accelIndex] else null
            val location = if (locationIndex < gyroReadings.size) gyroReadings[locationIndex] else null

            if (accel == null) {
                mergedList.add(MergedReading(location!!.timestampMillis, lastAccel, location))
                locationIndex++
            } else if (location == null) {
                mergedList.add(MergedReading(accel.timestampMillis, accel, lastLocation))
                accelIndex++
            } else {
                if (accel.timestampMillis < location.timestampMillis) {
                    mergedList.add(MergedReading(accel.timestampMillis, accel, lastLocation))
                    lastAccel = accel
                    accelIndex++
                } else if (accel.timestampMillis > location.timestampMillis) {
                    mergedList.add(MergedReading(location.timestampMillis, lastAccel, location))
                    lastLocation = location
                    locationIndex++
                } else {
                    mergedList.add(MergedReading(accel.timestampMillis, accel, location))
                    lastAccel = accel
                    lastLocation = location
                    accelIndex++
                    locationIndex++
                }
            }
        }

        return mergedList
    }


    data class MergedReading(
        val timestampMillis: Long,
        val accel: AccelerationReadingInfo?,
        val location: GyroInfoReading?
    )
}

class CarreraViewModelFactory(private val accelDao: AccelerationDao, private val gyroDao: GyroDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CarreraViewModel(accelDao, gyroDao) as T
}