package com.kuliah.greenhouse_iot.domain.usecases.controll

import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorRequest
import com.kuliah.greenhouse_iot.domain.repository.AktuatorRepository
import javax.inject.Inject

class PublishActuatorStatusUseCase @Inject constructor(
	private val repository: AktuatorRepository
) {
	suspend operator fun invoke(request: ActuatorRequest): Result<Unit> {
		return repository.publishActuatorStatus(request)
	}
}
