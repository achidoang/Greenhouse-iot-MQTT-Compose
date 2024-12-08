package com.kuliah.greenhouse_iot.data.remote.api.mode

import com.kuliah.greenhouse_iot.data.model.mode.ModeRequest
import com.kuliah.greenhouse_iot.data.model.mode.ModeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ModeApi {
	@POST("api/v2/mqtt/publish")
	suspend fun postMode(@Body request: ModeRequest): Response<ModeResponse>
}
