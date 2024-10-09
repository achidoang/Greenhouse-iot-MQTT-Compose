package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.LoginRequest
import com.kuliah.greenhouse_iot.data.model.LoginResponse
import com.kuliah.greenhouse_iot.data.model.RegisterRequest
import com.kuliah.greenhouse_iot.data.model.RegisterResponse
import com.kuliah.greenhouse_iot.data.model.User
import retrofit2.Response

interface UserRepository {
	suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse>
	suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>
	suspend fun getAllUsers(token: String): Response<List<User>>
}
