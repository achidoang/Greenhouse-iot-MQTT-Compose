package com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt

import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import javax.inject.Inject

class GetMonitoringUseCase @Inject constructor(
	private val repository: MonitoringRepository
) {
	operator fun invoke(onDataReceived: (MonitoringData) -> Unit, onError: (Throwable) -> Unit) {
		repository.subscribeToMonitoringTopic(onDataReceived, onError)
	}
}
