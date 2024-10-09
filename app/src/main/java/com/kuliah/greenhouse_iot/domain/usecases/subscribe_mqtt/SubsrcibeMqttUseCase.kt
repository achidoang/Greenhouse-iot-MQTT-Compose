package com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt

import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SubscribeMqttUseCase @Inject constructor(
	private val repository: MonitoringRepository
) {
	fun execute(): StateFlow<MonitoringData?> {
		return repository.subscribeToMonitoringData()
	}

	fun getErrorState(): StateFlow<String?> {
		return repository.getErrorState()
	}
}

