package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.mode.ModeRequest
import com.kuliah.greenhouse_iot.data.model.mode.ModeResponse
import com.kuliah.greenhouse_iot.data.remote.api.mode.ModeApi
import com.kuliah.greenhouse_iot.domain.repository.ModeRepository
import javax.inject.Inject

class ModeRepositoryImpl @Inject constructor(
	private val modeApi: ModeApi
) : ModeRepository {
	override suspend fun postMode(automode: Int): Result<ModeResponse> {
		return try {
			val response = modeApi.postMode(
				ModeRequest(
					topic = "herbalawu/mode",
					payload = mapOf("automode" to automode)
				)
			)
			if (response.isSuccessful) {
				Result.success(response.body() ?: ModeResponse(false, "Empty Response"))
			} else {
				Result.failure(Exception(response.message()))
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}
