package com.example.camc.view.all_sensors

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
import com.example.camc.model.room.entities.LocationReading
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

/**
 * Acceleration ViewModel
 * @param accelerationDao: dedicated database functionality wrapper
 * @property _accelerationState: data class represents 'config' state of the screen
 * @property _readings: private collection of most recent database entries
 *
 */
class AllSensorsViewModel(
    private val accelerationDao: AccelerationDao,
    private val gyroDao: GyroDao,
    private val magnetDao: MagnetDao,
    private val locationDao: LocationDao
): ViewModel() {
    private val _readingsAccel = accelerationDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _readingsGyro = gyroDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _readingsMagnet = magnetDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _readingsGps = locationDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(AllSensorsState())
    val state = combine(_state, _readingsAccel, _readingsGps)
    { screenState, _readingsAccel, _readingsGps ->
        screenState.copy (
            currentReadingsAccel = _readingsAccel,
            currentReadingsGPS = _readingsGps,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), AllSensorsState())
    companion object {
        val logger = Logger.getLogger(AllSensorsViewModel::class.java.name)
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

    fun onReceiveNewGpsReading(long: Double, lat: Double, speed: Float) {
        if (long == null || lat == null) return

        var reading = LocationReading(
            timestampMillis = System.currentTimeMillis(),
            long = long,
            lat = lat,
            altitude = 0.0, //values[2]
            velocity = speed,
            transportationMode = _state.value.transportationMode,
        )

        _state.update {
            it.copy(
                singleReadingGPS = reading
            )
        }

        var executed = false

        viewModelScope.launch {
            if (_state.first().isRecording && !executed) {
                locationDao.insertReading(reading)
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

    fun setSampleRateGps(sampleRate: Float) {
        _state.update {
            it.copy(
                sampleRateGpsMs = sampleRate
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
            locationDao.nukeReadings()
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

    suspend fun exportLocationReadingsToCsv(context: Context) {
        val readings = locationDao.getReadingInfoOrderedByTime().first()
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "location_readings.csv")

        FileWriter(file).use { writer ->
            writer.append("timestampMillis,velocity,transportationMode,sensor\n")
            readings.forEach { reading ->
                writer.append("${reading.timestampMillis},${reading.velocity},${reading.transportationMode ?: ""},${reading.sensor}\n")
            }
        }
    }
}

class AllSensorsViewModelFactory(private val accelDao: AccelerationDao, private val gyroDao: GyroDao, private val magnetDao: MagnetDao, private val gpsDao: LocationDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AllSensorsViewModel(accelDao, gyroDao, magnetDao, gpsDao) as T
}