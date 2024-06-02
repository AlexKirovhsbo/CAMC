package com.example.camc.view.all_sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.camc.model.room.dao.AccelerationDao
import com.example.camc.model.room.dao.GyroDao
import com.example.camc.model.room.dao.LocationDao
import com.example.camc.model.room.dao.MagnetDao
import com.example.camc.model.room.entities.AccelerationReading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.logging.Logger

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
    val state = combine(_state, _readingsAccel, _readingsGps, _readingsGyro, _readingsMagnet)
    { screenState, _readingsAccel, _readingsGps, _readingsGyro, _readingsMagnet ->
        screenState.copy (
            currentReadingsAccel = _readingsAccel,
            currentReadingsGPS = _readingsGps,
            currentReadingsGyro = _readingsGyro,
            currentReadingsMag = _readingsMagnet,
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
    fun onReceiveNewReading(values: FloatArray) {
        assert(values.size >= 3)

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

    fun setRepresentationMethod(method: Int) {
        _state.update {
            it.copy(
                representationMethod = method
            )
        }
    }

    fun setSampleRate(sampleRate: Int) {
        _state.update {
            it.copy(
                sampleRate = sampleRate
            )
        }
    }

    fun startRecording() {
        //should clean up
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
        }
    }
}

class AllSensorsViewModelFactory(private val accelDao: AccelerationDao, private val gyroDao: GyroDao, private val magnetDao: MagnetDao, private val gpsDao: LocationDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AllSensorsViewModel(accelDao, gyroDao, magnetDao, gpsDao) as T
}