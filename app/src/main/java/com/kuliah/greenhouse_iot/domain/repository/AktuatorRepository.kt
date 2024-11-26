package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorRequest
import com.kuliah.greenhouse_iot.data.remote.api.control.AktuatorApi
import javax.inject.Inject

interface AktuatorRepository {
	suspend fun publishActuatorStatus(request: ActuatorRequest): Result<Unit>
}


