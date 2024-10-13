package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.domain.repository.ConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
	private val mqttClientService: MqttClientService
) : ConnectionRepository {

	override suspend fun connectToBroker(): Result<Unit> {
		return try {
			mqttClientService.connect(
				onConnected = {},
				onError = {}
			)
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override suspend fun isConnected(): Boolean {
		return mqttClientService.isConnected()
	}
}
