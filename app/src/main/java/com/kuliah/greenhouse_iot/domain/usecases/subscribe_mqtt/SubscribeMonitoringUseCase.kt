package com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt

import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeMonitoringUseCase @Inject constructor(
	private val repository: MqttRepository
) {
	operator fun invoke() = repository.subscribeMonitoring()
}