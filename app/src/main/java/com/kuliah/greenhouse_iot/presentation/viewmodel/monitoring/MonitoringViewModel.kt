package com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.SubscribeMonitoringUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
	subscribeMonitoringUseCase: SubscribeMonitoringUseCase
) : ViewModel() {

//	val monitoringData: StateFlow<MonitoringData?> = subscribeMonitoringUseCase()
//		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
	val monitoringData: StateFlow<MonitoringData?> = subscribeMonitoringUseCase()
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)


	private fun <T> StateFlow<T?>.logData(tag: String) {
		viewModelScope.launch {
			collect { data ->
				Log.d(tag, "Data received: $data")
			}
		}
	}

	init {
		monitoringData.logData("MonitoringViewModel")
	}
}


