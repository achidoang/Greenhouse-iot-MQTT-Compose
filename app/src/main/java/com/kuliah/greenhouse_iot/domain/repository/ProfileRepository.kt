package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
	suspend fun getProfiles(): List<Profile>
	suspend fun createProfile(profile: Profile): Profile
	suspend fun updateProfile(id: Int, profile: Profile): Profile
	suspend fun deleteProfile(id: Int)
	suspend fun activateProfile(id: Int)
	fun observeProfiles(): Flow<List<Profile>> // Untuk WebSocket real-time
}
