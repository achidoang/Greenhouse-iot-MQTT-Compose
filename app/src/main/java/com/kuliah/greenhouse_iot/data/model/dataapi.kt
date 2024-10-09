package com.kuliah.greenhouse_iot.data.model

data class RegisterRequest(
	val username: String,
	val password: String,
	val role: String
)

data class RegisterResponse(
	val message: String
)

data class LoginRequest(
	val username: String,
	val password: String
)

data class LoginResponse(
	val token: String,
	val user: User
)

data class User(
	val id: String,
	val username: String,
	val role: String
)
