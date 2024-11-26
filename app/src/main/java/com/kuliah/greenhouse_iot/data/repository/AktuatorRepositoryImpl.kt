package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorRequest
import com.kuliah.greenhouse_iot.data.remote.api.control.AktuatorApi
import com.kuliah.greenhouse_iot.domain.repository.AktuatorRepository
import javax.inject.Inject

class AktuatorRepositoryImpl @Inject constructor(
	private val api: AktuatorApi
) : AktuatorRepository {
	override suspend fun publishActuatorStatus(request: ActuatorRequest): Result<Unit> {
		return try {
			val response = api.publishActuatorStatus(request)
			if (response.isSuccessful) Result.success(Unit)
			else Result.failure(Exception(response.errorBody()?.string()))
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}