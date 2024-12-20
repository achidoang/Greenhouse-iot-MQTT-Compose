package com.kuliah.greenhouse_iot.presentation.viewmodel.mode

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.local.datastore.ModePreferences
import com.kuliah.greenhouse_iot.data.model.mode.ModeResponse
import com.kuliah.greenhouse_iot.domain.usecases.mode.PostModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeViewModel @Inject constructor(
	private val postModeUseCase: PostModeUseCase,
	private val modePreferences: ModePreferences
) : ViewModel() {

	val isAutomaticMode: Flow<Boolean> = modePreferences.isAutomaticMode

	fun setAutomaticMode(isAutomatic: Boolean) {
		viewModelScope.launch {
			modePreferences.setAutomaticMode(isAutomatic)
			postMode(if (isAutomatic) 1 else 0)

			// Jika mode manual, mulai timer untuk kembali ke otomatis
			if (!isAutomatic) {
				startAutomaticModeTimer()
			}
		}
	}

	private fun startAutomaticModeTimer() {
		viewModelScope.launch {
			delay(100*10*1000L) // Delay 5 detik
			modePreferences.setAutomaticMode(true)
			postMode(1)
		}
	}

	private fun postMode(automode: Int) {
		viewModelScope.launch {
			postModeUseCase(automode)
		}
	}
}
