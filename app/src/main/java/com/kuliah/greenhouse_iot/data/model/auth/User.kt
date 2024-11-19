package com.kuliah.greenhouse_iot.data.model.auth

data class User(
	val id: Int? = null, // ID bisa opsional untuk beberapa kasus
	val username: String,
	val password: String,
	val email: String,
	val gender: String,
	val role: String,
	val fullname: String,
	val token: String?
)