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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
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


	fun loadProfiles() {
		viewModelScope.launch {
			try {
				val profiles = getProfilesUseCase()
				_uiState.value = ProfileUiState.Success(profiles)
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to load profiles.")
			}
		}
	}

	fun observeRealTimeProfiles() {
		viewModelScope.launch {
			observeProfilesUseCase()
				.catch { _uiState.value = ProfileUiState.Error("Failed to observe profiles.") }
				.collect { profiles ->
					_uiState.value = ProfileUiState.Success(profiles)
				}
		}
	}

	fun createProfile(profile: Profile) {
		viewModelScope.launch {
			try {
				createProfileUseCase(profile)
				loadProfiles()
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to create profile.")
			}
		}
	}

	fun updateProfile(id: Int, profile: Profile) {
		viewModelScope.launch {
			try {
				updateProfileUseCase(id, profile)
				loadProfiles()
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to update profile.")
			}
		}
	}

	fun deleteProfile(id: Int) {
		viewModelScope.launch {
			try {
				deleteProfileUseCase(id)
				loadProfiles()
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to delete profile.")
			}
		}
	}

	fun activateProfile(id: Int) {
		viewModelScope.launch {
			try {
				activateProfileUseCase(id)
				loadProfiles()
			} catch (e: Exception) {
				_uiState.value = ProfileUiState.Error("Failed to activate profile.")
			}
		}
	}
}

sealed class ProfileUiState {
	object Loading : ProfileUiState()
	data class Success(val profiles: List<Profile>) : ProfileUiState()
	data class Error(val message: String) : ProfileUiState()
}
