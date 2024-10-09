package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.LoginRequest
import com.kuliah.greenhouse_iot.data.model.LoginResponse
import com.kuliah.greenhouse_iot.data.model.RegisterRequest
import com.kuliah.greenhouse_iot.data.model.RegisterResponse
import com.kuliah.greenhouse_iot.data.model.User
import com.kuliah.greenhouse_iot.data.remote.ApiService
import com.kuliah.greenhouse_iot.domain.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
	private val apiService: ApiService
) : UserRepository {
	override suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse> {
		return apiService.registerUser(registerRequest)
	}

	override suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
		return apiService.loginUser(loginRequest)
	}

	override suspend fun getAllUsers(token: String): Response<List<User>> {
		return apiService.getAllUsers(token)
	}
}
