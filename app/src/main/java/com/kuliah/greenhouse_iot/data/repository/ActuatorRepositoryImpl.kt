package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.domain.repository.ActuatorRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ActuatorRepositoryImpl @Inject constructor(
	private val mqttClientService: MqttClientService
) : ActuatorRepository {

	override suspend fun getActuatorStatus(): ActuatorStatus {
		return suspendCoroutine { continuation ->
			mqttClientService.getActuatorStatusFromMqtt(
				onStatusReceived = { continuation.resume(it) },
				onError = { continuation.resumeWithException(it!!) }
			)
		}
	}

	override suspend fun publishActuatorStatus(actuatorStatus: ActuatorStatus): Result<Unit> {
		return suspendCoroutine { continuation ->
			mqttClientService.publishActuatorStatusToMqtt(
				actuatorStatus = actuatorStatus,
				onSuccess = { continuation.resume(Result.success(Unit)) },
				onError = { continuation.resume(Result.failure(it!!)) }
			)
		}
	}
}

