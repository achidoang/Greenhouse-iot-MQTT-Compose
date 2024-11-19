package com.kuliah.greenhouse_iot.domain.usecases.user

import com.kuliah.greenhouse_iot.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
	private val userRepository: UserRepository
) {
	suspend operator fun invoke(userId: Int): Boolean {
		return userRepository.deleteUser(userId)
	}
}
