package com.kuliah.greenhouse_iot.domain.usecases.controll_mqtt

import com.kuliah.greenhouse_iot.data.model.Setpoints
import com.kuliah.greenhouse_iot.domain.repository.SetpointRepository
import javax.inject.Inject

class PublishSetpointsUseCase @Inject constructor(
	private val repository: SetpointRepository
) {
	suspend operator fun invoke(setpoints: Setpoints): Result<Unit> {
		return repository.publishSetpoints(setpoints)
	}
}
