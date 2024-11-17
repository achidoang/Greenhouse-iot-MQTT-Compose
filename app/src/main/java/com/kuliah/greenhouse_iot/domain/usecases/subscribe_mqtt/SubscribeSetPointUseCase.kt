package com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt

import com.kuliah.greenhouse_iot.data.model.SetPointData
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeSetPointUseCase @Inject constructor(
	private val repository: MqttRepository
) {
	operator fun invoke(): Flow<SetPointData> = repository.subscribeSetPoint()
}