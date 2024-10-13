package com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuliah.greenhouse_iot.data.local.monitoring.MonitoringPreferencesManager
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.data.repository.MonitoringRepositoryImpl
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.GetMonitoringUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MonitoringViewModel @Inject constructor(
	private val getMonitoringUseCase: GetMonitoringUseCase,
	private val monitoringPreferencesManager: MonitoringPreferencesManager
) : ViewModel() {

	private val _monitoringData = MutableLiveData<MonitoringData>()
	val monitoringData: LiveData<MonitoringData> = _monitoringData

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	// Coroutine scope to manage periodic data updates
	private val viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	init {
		// Load saved monitoring data from DataStore
		viewModelScope.launch {
			monitoringPreferencesManager.getMonitoring().collect { savedMonitoringData ->
				_monitoringData.postValue(savedMonitoringData)
			}
		}
		// Subscribe to new data
		subscribeToMonitoring()
	}

	private fun subscribeToMonitoring() {
		getMonitoringUseCase(
			onDataReceived = { data ->
				// Call function to periodically update data
				updateDataPeriodically(data)
			},
			onError = { throwable ->
				_error.postValue(throwable)
			}
		)
	}

	// Update data only once every second
	private fun updateDataPeriodically(data: MonitoringData) {
		viewModelScope.launch {
			// Delay for 1 second before updating data
			delay(1000)

			// Update monitoring data
			_monitoringData.postValue(data)

			// Save the monitoring data to DataStore
			saveMonitoringToDataStore(data)
		}
	}

	private fun saveMonitoringToDataStore(monitoringData: MonitoringData) {
		viewModelScope.launch {
			monitoringPreferencesManager.saveMonitoring(monitoringData)
		}
	}

	override fun onCleared() {
		super.onCleared()
		// Cancel all coroutines when ViewModel is destroyed
		viewModelScope.cancel()
	}
}


