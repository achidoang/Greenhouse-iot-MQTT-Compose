package com.kuliah.greenhouse_iot.data.remote.api.control

import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AktuatorApi {
	@POST("api/v2/mqtt/publish")
	suspend fun publishActuatorStatus(@Body request: ActuatorRequest): Response<Unit>
}
