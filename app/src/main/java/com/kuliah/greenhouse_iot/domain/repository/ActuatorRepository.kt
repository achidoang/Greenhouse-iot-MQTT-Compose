package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ActuatorRepository {
	suspend fun getActuatorStatus(): ActuatorStatus
	suspend fun publishActuatorStatus(actuatorStatus: ActuatorStatus): Result<Unit>
}
