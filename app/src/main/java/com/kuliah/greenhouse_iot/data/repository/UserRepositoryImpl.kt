package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.data.remote.api.akun.UserApi
import com.kuliah.greenhouse_iot.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
	private val api: UserApi // Retrofit interface
) : UserRepository {
	override suspend fun getAllUsers(): List<User> {
		return api.getAllUsers()
	}

	override suspend fun getUserById(userId: Int): User {
		return api.getUserById(userId)
	}

	override suspend fun addUser(user: User): User {
		return api.addUser(user)
	}

	override suspend fun updateUser(userId: Int, user: User): User {
		return api.updateUser(userId, user)
	}

	override suspend fun deleteUser(userId: Int): Boolean {
		return api.deleteUser(userId).success
	}
}
