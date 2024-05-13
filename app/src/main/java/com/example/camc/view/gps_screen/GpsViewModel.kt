package com.example.camc.view.gps_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.camc.model.room.dao.LocationDao
import com.example.camc.model.room.entities.LocationReading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * GPS ViewModel
 * @param locationDao: dedicated database functionality wrapper
 * @property _locationState: data class represents 'config' state of the screen
 * @property _readings: private collection of most recent database entries
 *
 */
class GpsViewModel(
    private val locationDao: LocationDao
) : ViewModel() {
    private val _readings = locationDao.getReadingsOrderedByTime()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(LocationState())
    val state = combine(_state, _readings) { screenState, readings ->
        screenState.copy(
            currentReadings = readings
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), LocationState())


    /* TODO: Generate more methods for state-mutation */
    /**
     * function onRecieveNewReading: sets state's current reading to new inserted reading
     *          conditional: if state is set to 'recording', the new datapoint is written into
     *                       the database
     * @param values: Is the new reading with which the state and database are updated
     *
     * NOTE: As there are 3 parts to the reading, the function should fail if the number differs
     */
    fun onReceiveNewReading(long: Double, lat: Double) {
        if (long == null || lat == null) return

        var reading = LocationReading(
            timestampMillis = System.currentTimeMillis(),
            long = long,
            lat = lat,
            altitude = 0.0 //values[2]
        )

        _state.update {
            it.copy(
                singleReading = reading
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

    fun setSampleRate(sampleRate: Float) {
        _state.update {
            it.copy(
                sampleRateMs = sampleRate
            )
        }
    }

    fun setMeterSelection(meter: Float) {
        _state.update {
            it.copy(
                meterSelection = meter
            )
        }
    }

    fun setSingleReading(singleReading: LocationReading) {
        _state.update {
            it.copy(
                singleReading = singleReading,
                triggerUpdate = !it.triggerUpdate
            )
        }
    }

    fun setRepresentationMethod(representation: Int) {
        _state.update {
            it.copy(
                representationMethod = representation
            )
        }
    }

    fun setProvider(provider: String) {
        _state.update {
            it.copy(
                provider = provider
            )
        }
    }

    fun startRecording() {
        //nukeReadings()

        var executed = false
        viewModelScope.launch {
            _state.collectLatest {
                if (!executed) {
                    locationDao.insertReading(it.singleReading)
                    executed = true
                }
            }
        }

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
            locationDao.nukeReadings()
        }
    }


}

class GpsViewModelFactory(private val dao: LocationDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = GpsViewModel(dao) as T
}