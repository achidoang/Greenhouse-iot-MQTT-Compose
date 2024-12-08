package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorRequest

interface AktuatorRepository {
	suspend fun publishActuatorStatus(request: ActuatorRequest): Result<Unit>
}


