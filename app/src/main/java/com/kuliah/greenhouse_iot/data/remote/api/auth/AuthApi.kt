package com.kuliah.greenhouse_iot.data.remote.api.auth

import com.kuliah.greenhouse_iot.data.model.auth.LoginRequest
import com.kuliah.greenhouse_iot.data.model.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
	@POST("/api/v2/auth/login")
	suspend fun login(@Body request: LoginRequest): LoginResponse

}

