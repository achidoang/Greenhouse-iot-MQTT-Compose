package com.kuliah.greenhouse_iot.data.repository

import android.util.Log
import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.data.remote.api.profile.ProfileApi
import com.kuliah.greenhouse_iot.data.websocket.WebSocketClient
import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileRepositoryImpl(
	private val apiService: ProfileApi,
	private val webSocketClient: WebSocketClient
) : ProfileRepository {

	private val _profilesFlow = MutableStateFlow<List<Profile>>(emptyList())

	init {
		webSocketClient.startListening { message ->
			if (message.has("type") && message.getString("type") == "PROFILE_UPDATE") {
				val updatedProfiles = message.getJSONArray("data").let { array ->
					List(array.length()) { i ->
						val json = array.getJSONObject(i)
						Profile(
							id = json.getInt("id"),
							watertemp = json.getDouble("watertemp"),
							waterppm = json.getDouble("waterppm"),
							waterph = json.getDouble("waterph"),
							profile = json.getString("profile"),
							status = if (json.getInt("status") == 1) "active" else "inactive",
							timestamp = json.getString("timestamp")
						)
					}
				}
				_profilesFlow.value = updatedProfiles
			}
		}
	}


	override suspend fun createProfile(profile: Profile): Profile = apiService.createProfile(profile)

	override suspend fun updateProfile(id: Int, profile: Profile): Profile =
		apiService.updateProfile(id, profile)

	override suspend fun deleteProfile(id: Int) = apiService.deleteProfile(id)

	override suspend fun activateProfile(id: Int) = apiService.activateProfile(id)

	override fun observeProfiles(): Flow<List<Profile>> = _profilesFlow

	override suspend fun getProfiles(): List<Profile> {
		return try {
			val response = apiService.getProfiles()
			Log.d("ProfileRepository", "Profiles: $response")
			response
		} catch (e: Exception) {
			Log.e("ProfileRepository", "Failed to fetch profiles: ${e.message}", e)
			emptyList() // Handle failure gracefully
		}
	}

}
