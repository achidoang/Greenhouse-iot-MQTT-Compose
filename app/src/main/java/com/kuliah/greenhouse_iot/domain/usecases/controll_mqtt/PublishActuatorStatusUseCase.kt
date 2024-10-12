package com.kuliah.greenhouse_iot.domain.usecases.controll_mqtt

import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.domain.repository.ActuatorRepository
import javax.inject.Inject

class PublishActuatorStatusUseCase @Inject constructor(
	private val repository: ActuatorRepository
) {
	suspend operator fun invoke(actuatorStatus: ActuatorStatus): Result<Unit> {
		return repository.publishActuatorStatus(actuatorStatus)
	}
}

