package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.Setpoints
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.domain.repository.SetpointRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SetpointRepositoryImpl @Inject constructor(
	private val mqttClientService: MqttClientService
) : SetpointRepository {

	override suspend fun getSetpoints(): Setpoints {
		return suspendCoroutine { continuation ->
			mqttClientService.getSetpointsFromMqtt(
				onSetpointsReceived = { continuation.resume(it) },
				onError = { continuation.resumeWithException(it!!) }
			)
		}
	}

	override suspend fun publishSetpoints(setpoints: Setpoints): Result<Unit> {
		return suspendCoroutine { continuation ->
			mqttClientService.publishSetpointsToMqtt(
				setpoints = setpoints,
				onSuccess = { continuation.resume(Result.success(Unit)) },
				onError = { continuation.resume(Result.failure(it!!)) }
			)
		}
	}
}

