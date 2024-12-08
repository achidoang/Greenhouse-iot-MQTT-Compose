package com.kuliah.greenhouse_iot.presentation.viewmodel.mode

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.mode.ModeResponse
import com.kuliah.greenhouse_iot.domain.usecases.mode.PostModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeViewModel @Inject constructor(
	private val postModeUseCase: PostModeUseCase
) : ViewModel() {

	private val _modeResponse = mutableStateOf<Result<ModeResponse>?>(null)
	val modeResponse: State<Result<ModeResponse>?> = _modeResponse

	fun postMode(automode: Int) {
		viewModelScope.launch {
			_modeResponse.value = postModeUseCase(automode)
		}
	}
}
