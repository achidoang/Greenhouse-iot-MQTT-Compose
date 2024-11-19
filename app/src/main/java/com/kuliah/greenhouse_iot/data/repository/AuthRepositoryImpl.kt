package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.auth.LoginRequest
import com.kuliah.greenhouse_iot.data.model.auth.LoginResponse
import com.kuliah.greenhouse_iot.data.remote.api.auth.AuthApi
import com.kuliah.greenhouse_iot.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val authApi: AuthApi
) : AuthRepository {

	override suspend fun login(request: LoginRequest): LoginResponse {
		println("Sending Login Request: $request")
		val response = authApi.login(request)
		println("Received Login Response: $response")
		return response
	}
}