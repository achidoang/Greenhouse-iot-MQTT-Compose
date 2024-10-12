package com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt

import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.domain.repository.ActuatorRepository
import javax.inject.Inject

class GetActuatorStatusUseCase @Inject constructor(
	private val repository: ActuatorRepository
) {
	suspend operator fun invoke(): ActuatorStatus {
		return repository.getActuatorStatus()
	}
}
