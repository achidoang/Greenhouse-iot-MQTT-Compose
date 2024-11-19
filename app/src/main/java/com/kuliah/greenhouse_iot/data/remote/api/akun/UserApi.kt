package com.kuliah.greenhouse_iot.data.remote.api.akun

import com.kuliah.greenhouse_iot.data.model.auth.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
	@GET("/api/v2/users")
	suspend fun getAllUsers(): List<User>

	@GET("/api/v2/users/{id}")
	suspend fun getUserById(@Path("id") userId: Int): User

	@POST("/api/v2/auth/add-account")
	suspend fun addUser(@Body user: User): User

	@PUT("/api/v2/users/{id}")
	suspend fun updateUser(@Path("id") userId: Int, @Body user: User): User

	@DELETE("/api/v2/users/{id}")
	suspend fun deleteUser(@Path("id") userId: Int): DeleteResponse
}

data class DeleteResponse(val success: Boolean)
