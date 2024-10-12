package com.kuliah.greenhouse_iot.presentation.viewmodel.mqtt

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.data.repository.MonitoringRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MqttViewModel @Inject constructor(
	private val repository: MonitoringRepositoryImpl,
) : ViewModel() {

	private val _monitoringData = MutableStateFlow<MonitoringData?>(null)
	val monitoringData: StateFlow<MonitoringData?> = _monitoringData

	private val _errorState = MutableStateFlow<String?>(null)
	val errorState: StateFlow<String?> = _errorState


	init {
		Log.d("MqttViewModel", "ViewModel initialized, starting to listen to MQTT")
		repository.startListening()

		// Observe data from repository and update the StateFlows accordingly
		viewModelScope.launch {
			repository.subscribeToMonitoringData().collect { data ->
				_monitoringData.value = data
			}
		}
		viewModelScope.launch {
			repository.getErrorState().collect { error ->
				_errorState.value = error
			}
		}
	}
}


