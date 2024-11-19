package com.kuliah.greenhouse_iot.domain.usecases.login

import com.kuliah.greenhouse_iot.data.model.auth.LoginRequest
import com.kuliah.greenhouse_iot.data.model.auth.LoginResponse
import com.kuliah.greenhouse_iot.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(username: String, password: String): LoginResponse {
		println("LoginUseCase - Username: $username, Password: $password")
		val response = authRepository.login(LoginRequest(username, password))
		println("LoginUseCase - Token Received: ${response.token}")
		return response
	}
}