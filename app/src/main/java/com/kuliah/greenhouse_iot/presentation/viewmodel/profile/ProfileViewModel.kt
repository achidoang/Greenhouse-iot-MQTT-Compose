package com.kuliah.greenhouse_iot.presentation.viewmodel.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.domain.usecases.profile.ActivateProfileUseCase
import com.kuliah.greenhouse_iot.domain.usecases.profile.CreateProfileUseCase
import com.kuliah.greenhouse_iot.domain.usecases.profile.DeleteProfileUseCase
import com.kuliah.greenhouse_iot.domain.usecases.profile.GetProfilesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.profile.ObserveProfilesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.profile.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val getProfilesUseCase: GetProfilesUseCase,
	private val createProfileUseCase: CreateProfileUseCase,
	private val updateProfileUseCase: UpdateProfileUseCase,
	private val deleteProfileUseCase: DeleteProfileUseCase,
	private val activateProfileUseCase: ActivateProfileUseCase,
	private val observeProfilesUseCase: ObserveProfilesUseCase
) : ViewModel() {

	private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
	val uiState: StateFlow<ProfileUiState> = _uiState

	private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
	val profiles: StateFlow<List<Profile>> = _profiles

	private val _sortType = MutableStateFlow(SortType.ALPHABETICAL)
	val sortType: StateFlow<SortType> = _sortType


	init {
		observeRealTimeProfiles()
	}

	fun loadProfiles() {
		viewModelScope.launch {
			try {
				val profiles = getProfilesUseCase()
				_profiles.value = profiles
				_uiState.value = ProfileUiState.Success(profiles)
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to load profiles.")
			}
		}
	}

	private fun observeRealTimeProfiles() {
		viewModelScope.launch {
			observeProfilesUseCase()
				.retryWhen { cause, attempt ->
					if (cause is IOException) {
						delay(2000) // Retry jika terjadi error
						true
					} else {
						false
					}
				}
				.catch { e ->
					_uiState.value = ProfileUiState.Error("Failed to observe profiles: ${e.message}")
				}
				.collect { profiles ->
					_profiles.value = profiles
					_uiState.value = ProfileUiState.Success(profiles)
				}
		}
	}

	fun createProfile(profile: Profile) {
		viewModelScope.launch {
			try {
				createProfileUseCase(profile)
				// Real-time observe akan menangani pembaruan UI
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to create profile.")
			}
		}
	}

	fun updateProfile(id: Int, profile: Profile) {
		viewModelScope.launch {
			try {
				updateProfileUseCase(id, profile)
				// Real-time observe akan menangani pembaruan UI
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to update profile.")
			}
		}
	}


	fun deleteProfile(id: Int) {
		viewModelScope.launch {
			try {
				deleteProfileUseCase(id)
				loadProfiles() // Reload data setelah profil dihapus
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to delete profile.")
			}
		}
	}


	fun activateProfile(id: Int) {
		viewModelScope.launch {
			try {
				updateLocalProfileStatus(id)
				activateProfileUseCase(id)
				// Real-time observe akan menangani pembaruan UI
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to activate profile.")
			}
		}
	}

	private fun updateLocalProfileStatus(activatedId: Int) {
		_profiles.update { currentProfiles ->
			currentProfiles.map { profile ->
				profile.copy(status = if (profile.id == activatedId) 1 else 0)
			}
		}
		_uiState.value = ProfileUiState.Success(_profiles.value)
	}

	fun setSortType(type: SortType) {
		_sortType.value = type
		sortProfiles()
	}

	private fun sortProfiles() {
		_profiles.update { currentProfiles ->
			when (_sortType.value) {
				SortType.ALPHABETICAL -> currentProfiles.sortedBy { it.profile }
				SortType.TIMESTAMP -> currentProfiles.sortedByDescending { it.timestamp }
				SortType.CUSTOM -> currentProfiles // Custom sorting handled during drag-and-drop
			}
		}
	}
}

sealed class ProfileUiState {
	object Loading : ProfileUiState()
	data class Success(val profiles: List<Profile>) : ProfileUiState()
	data class Error(val message: String) : ProfileUiState()
}

enum class SortType {
	ALPHABETICAL,
	TIMESTAMP,
	CUSTOM
}

