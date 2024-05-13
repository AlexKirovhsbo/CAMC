package com.example.camc.view.gyroscope_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.camc.model.room.dao.GyroDao
import com.example.camc.model.room.entities.GyroReading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Gyro ViewModel
 * @param GyroDao: dedicated database functionality wrapper
 * @property _gyroState: data class represents 'config' state of the screen
 * @property _readings: private collection of most recent database entries
 *
 */
class GyroViewModel(
    private val gyroDao: GyroDao
): ViewModel() {
    private val _readings = gyroDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(GyroState())
    val state = combine(_state, _readings) { screenState, readings ->
        screenState.copy (
            currentReadings = readings
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), GyroState())


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

        var reading = GyroReading(
            timestampMillis = System.currentTimeMillis(),
            xAxis = values[0],
            yAxis = values[1],
            zAxis = values[2]
        )

        _state.update {
            it.copy(
                singleReading = reading
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

    fun setRepresentationMethod(method: Int) {
        _state.update {
            it.copy(
                representationMethod = method
            )
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
            gyroDao.nukeReadings()
        }
    }
}

class GyroViewModelFactory(private val dao: GyroDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = GyroViewModel(dao) as T
}