package com.kuliah.greenhouse_iot.data.remote.api.profile

import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApi {
	@GET("api/v2/profiles")
	suspend fun getProfiles(): List<Profile>

	@POST("api/v2/profiles")
	suspend fun createProfile(@Body profile: Profile): Profile

	@PUT("api/v2/profiles/{id}")
	suspend fun updateProfile(@Path("id") id: Int, @Body profile: Profile): Profile

	@DELETE("api/v2/profiles/{id}")
	suspend fun deleteProfile(@Path("id") id: Int)

	@PUT("api/v2/profiles/{id}/activate")
	suspend fun activateProfile(@Path("id") id: Int)
}