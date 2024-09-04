package com.example.camc.view.main_screen

import android.telephony.SmsManager
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.camc.model.room.dao.AccelerationDao
import com.example.camc.model.room.dao.GyroDao
import com.example.camc.model.room.dao.LocationDao
import com.example.camc.model.room.dao.MagnetDao
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.LocationReading
import com.example.camc.view.all_sensors.AllSensorsState
import com.example.camc.view.all_sensors.AllSensorsViewModel
import com.example.camc.view.all_sensors.WekaWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import weka.core.Attribute
import weka.core.DenseInstance
import weka.core.Instances
import java.util.logging.Logger
import kotlin.math.sqrt


class MainViewModel(
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
    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    val selectedOptions = mutableStateListOf<String>()
    val mobileNumber = mutableStateOf("")

    private val _latestClassification = MutableStateFlow<String>("")
    val latestClassification: StateFlow<String> = _latestClassification
    private val _state = MutableStateFlow(StaySafeState())
    val state = combine(_state, _readingsAccel, _readingsGps)
    { screenState, _readingsAccel, _readingsGps ->
        screenState.copy (
            currentReadingsAccel = _readingsAccel,
            currentReadingsGPS = _readingsGps,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), StaySafeState())
    companion object {
        val logger = Logger.getLogger(AllSensorsViewModel::class.java.name)
    }
    val weka = WekaWrapper()

    private var lastClassificationTime: Long = 0
    private val classificationInterval: Long = 15000 // 15 seconds

    fun addSelection(option: String) {
        if (!selectedOptions.contains(option)) {
            selectedOptions.add(option)
        }
    }

    fun removeSelection(option: String) {
        selectedOptions.remove(option)
    }

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun saveMobileNumber(number: String) {
        mobileNumber.value = number
    }

    fun onReceiveNewAccelReading(values: FloatArray) {
        assert(values.size >= 3)

        val reading = AccelerationReading(
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

        viewModelScope.launch {
            if (_state.first().isRecording) {
                accelerationDao.insertReading(reading)
                classifyLatestData()
            }
        }
    }

    fun startStopRecording() {
        _state.update {
            it.copy(
                isRecording = !it.isRecording
            )
        }
        if (!_state.value.isRecording) {
            _latestClassification.value = ""
        }
    }

    fun onReceiveNewGpsReading(long: Double, lat: Double, speed: Float, bearing: Float) {
        if (long == null || lat == null) return

        val reading = LocationReading(
            timestampMillis = System.currentTimeMillis(),
            long = long,
            lat = lat,
            altitude = 0.0, //values[2]
            velocity = speed,
            bearing = bearing,
            transportationMode = _state.value.transportationMode,
        )

        _state.update {
            it.copy(
                singleReadingGPS = reading
            )
        }

        viewModelScope.launch {
            if (_state.first().isRecording) {
                locationDao.insertReading(reading)
                classifyLatestData()
            }
        }
    }

    fun classifyLatestData() {
        val currAccelReading = _state.value.singleReadingAccel
        val magnitude = sqrt((currAccelReading.xAxis * currAccelReading.xAxis + currAccelReading.yAxis * currAccelReading.yAxis + currAccelReading.zAxis * currAccelReading.zAxis).toDouble())
        val currGpsSpeed = _state.value.singleReadingGPS.velocity.toDouble()

        val attributes = arrayListOf(
            Attribute("bewegungsart", listOf("Gehen", "Laufen", "Stehen", "Auto", "Zug", "")),
            Attribute("geschwindigkeit"),
            Attribute("Magnitude"),
        )
        val dataSet = Instances("TestInstances", attributes, 0)
        dataSet.setClassIndex(dataSet.numAttributes() - 1)

        val newInstance = DenseInstance(dataSet.numAttributes())
        newInstance.setValue(attributes[0], "")
        newInstance.setValue(attributes[1], currGpsSpeed)
        newInstance.setValue(attributes[2], magnitude)

        newInstance.setDataset(dataSet)
        val classified = weka.classifyInstance(newInstance)

        val resultString = when (classified) {
            1.0 -> "Stehen"
            2.0 -> "Gehen"
            0.0 -> "Laufen"
            3.0 -> "Auto"
            4.0 -> "Zug"
            else -> "Unknown"
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClassificationTime >= classificationInterval && !selectedOptions.contains(resultString)) {
            val messageToSend = "Ich bewege mich gerade mit einer unerwarteten Methode fort: $resultString. Melde dich doch mal bei mir"
            val number = mobileNumber.value
            SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null)
            lastClassificationTime = currentTime
        }

        _latestClassification.value = "Fortbewegungsart: $resultString"
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

}


class MainViewModelFactory(private val accelDao: AccelerationDao, private val gyroDao: GyroDao, private val magnetDao: MagnetDao, private val gpsDao: LocationDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(accelDao, gyroDao, magnetDao, gpsDao) as T
}