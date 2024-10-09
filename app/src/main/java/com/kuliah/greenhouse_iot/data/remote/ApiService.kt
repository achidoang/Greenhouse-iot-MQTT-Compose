package com.kuliah.greenhouse_iot.data.remote

import com.kuliah.greenhouse_iot.data.model.LoginResponse
import com.kuliah.greenhouse_iot.data.model.RegisterRequest
import com.kuliah.greenhouse_iot.data.model.RegisterResponse
import com.kuliah.greenhouse_iot.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

	@POST("auth/register")
	suspend fun registerUser(
		@Body registerRequest: RegisterRequest
	): Response<RegisterResponse>

	@POST("auth/login")
	suspend fun <LoginRequest> loginUser(
		@Body loginRequest: LoginRequest
	): Response<LoginResponse>

	@GET("users")
	suspend fun getAllUsers(
		@Header("Authorization") token: String
	): Response<List<User>>

	// Tambahkan API lainnya sesuai dokumentasi...
}