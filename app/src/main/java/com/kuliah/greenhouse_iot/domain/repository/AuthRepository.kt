package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.auth.LoginRequest
import com.kuliah.greenhouse_iot.data.model.auth.LoginResponse

interface AuthRepository {
	suspend fun login(request: LoginRequest): LoginResponse
}