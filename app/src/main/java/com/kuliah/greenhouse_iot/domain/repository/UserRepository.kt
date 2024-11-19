package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.auth.User

interface UserRepository {
	suspend fun getAllUsers(): List<User> // Admin only
	suspend fun getUserById(userId: Int): User // Admin/User (self)
	suspend fun addUser(user: User): User // Admin only
	suspend fun updateUser(userId: Int, user: User): User // Admin only
	suspend fun deleteUser(userId: Int): Boolean // Admin only
}
