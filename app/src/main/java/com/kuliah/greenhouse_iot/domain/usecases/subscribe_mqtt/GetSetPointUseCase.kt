package com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt

import com.kuliah.greenhouse_iot.data.model.Setpoints
import com.kuliah.greenhouse_iot.domain.repository.SetpointRepository
import javax.inject.Inject

class GetSetpointsUseCase @Inject constructor(
	private val repository: SetpointRepository
) {
	suspend operator fun invoke(): Setpoints {
		return repository.getSetpoints()
	}
}

