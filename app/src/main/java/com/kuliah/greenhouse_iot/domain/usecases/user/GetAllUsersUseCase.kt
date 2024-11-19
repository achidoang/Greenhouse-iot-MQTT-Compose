package com.kuliah.greenhouse_iot.domain.usecases.user

import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.domain.repository.UserRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
	private val userRepository: UserRepository
) {
	suspend operator fun invoke(): List<User> {
		return userRepository.getAllUsers()
	}
}
