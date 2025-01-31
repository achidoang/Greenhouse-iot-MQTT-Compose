package com.kuliah.greenhouse_iot.presentation.viewmodel.actuator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorPayload
import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorRequest
import com.kuliah.greenhouse_iot.data.model.subscribe.AktuatorData
import com.kuliah.greenhouse_iot.data.model.subscribe.toActuatorPayload
import com.kuliah.greenhouse_iot.domain.usecases.controll.PublishActuatorStatusUseCase
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.SubscribeAktuatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ControllAktuatorViewModel @Inject constructor(
	private val publishActuatorStatusUseCase: PublishActuatorStatusUseCase,
	private val subscribeAktuatorUseCase: SubscribeAktuatorUseCase
) : ViewModel() {
	private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
	val uiState: StateFlow<UiState> = _uiState

	private val _aktuatorState = MutableStateFlow<ActuatorPayload?>(null)
	val aktuatorState: StateFlow<ActuatorPayload?> = _aktuatorState

	init {
		subscribeToAktuator()
	}

	private fun subscribeToAktuator() {
		viewModelScope.launch {
			subscribeAktuatorUseCase()
				.catch { e ->
					Log.e("Aktuator", "Error subscribing to aktuator: ${e.message}")
				}
				.collect { data ->
					Log.d("Aktuator", "Received data: $data")
					_aktuatorState.value = data.toActuatorPayload()
				}
		}
	}

	fun toggleActuatorStatus(payload: ActuatorPayload) {
		viewModelScope.launch {
			_uiState.value = UiState.Loading

			val request = ActuatorRequest(
				topic = "herbalawu/aktuator",
				payload = payload
			)

			Log.d("ActuatorRequest", "Request: $request") // Debug request
			val previousState = _aktuatorState.value
			val result = publishActuatorStatusUseCase(request)

			if (result.isSuccess) {
				Log.d("ActuatorResponse", "Success")
				_uiState.value = UiState.Success
				delay(2500)
				if (_aktuatorState.value == previousState) {
					// Jika data tidak berubah, anggap perangkat tidak menanggapi
					_uiState.value = UiState.NoResponse
				} else {
					_uiState.value = UiState.Idle
				}
			} else {
				Log.e("ActuatorResponse", "Error: ${result.exceptionOrNull()}")
				_uiState.value = UiState.Error
			}

			delay(1500)
			_uiState.value = UiState.Idle
		}
	}
}



sealed class UiState {
	object Idle : UiState()
	object Loading : UiState()
	object Success : UiState()
	object Error : UiState()
	object NoResponse : UiState()
}
