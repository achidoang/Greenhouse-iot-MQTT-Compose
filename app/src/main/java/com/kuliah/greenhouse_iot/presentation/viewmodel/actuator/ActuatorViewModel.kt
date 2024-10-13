package com.kuliah.greenhouse_iot.presentation.viewmodel.actuator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.local.actuator.ActuatorPreferencesManager
import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.domain.usecases.controll_mqtt.PublishActuatorStatusUseCase
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.GetActuatorStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ActuatorViewModel @Inject constructor(
	private val getActuatorStatusUseCase: GetActuatorStatusUseCase,
	private val publishActuatorStatusUseCase: PublishActuatorStatusUseCase,
	private val mqttClientService: MqttClientService,
	private val actuatorPreferencesManager: ActuatorPreferencesManager
) : ViewModel() {

	private val _actuatorStatus = MutableStateFlow(ActuatorStatus())
	val actuatorStatus: StateFlow<ActuatorStatus> = _actuatorStatus

	private val _editedActuatorStatus = MutableStateFlow(ActuatorStatus())
	val editedActuatorStatus: StateFlow<ActuatorStatus> = _editedActuatorStatus

	// Muat status actuator dari DataStore saat ViewModel dibuat
	init {
		viewModelScope.launch {
			actuatorPreferencesManager.getActuatorStatus().collect { savedStatus ->
				_actuatorStatus.value = savedStatus
				_editedActuatorStatus.value = savedStatus
			}
		}
		subscribeToActuatorUpdates()
	}


	private fun subscribeToActuatorUpdates() {
		mqttClientService.subscribe("herbalawu/aktuator", 1) { message ->
			try {
				val updatedStatus = parseActuatorMessage(message)
				_actuatorStatus.value = updatedStatus
				_editedActuatorStatus.value = updatedStatus

				// Simpan status terbaru ke DataStore
				viewModelScope.launch {
					actuatorPreferencesManager.saveActuatorStatus(updatedStatus)
				}
			} catch (e: Exception) {
				Log.e("ActuatorViewModel", "Invalid MQTT message: ${e.message}")
			}
		}
	}

	fun toggleActuator(actuatorKey: String, value: Int) {
		_editedActuatorStatus.value = when (actuatorKey) {
			"actuator_nutrisi" -> _editedActuatorStatus.value.copy(actuator_nutrisi = value)
			"actuator_ph_up" -> _editedActuatorStatus.value.copy(actuator_ph_up = value)
			"actuator_ph_down" -> _editedActuatorStatus.value.copy(actuator_ph_down = value)
			"actuator_air_baku" -> _editedActuatorStatus.value.copy(actuator_air_baku = value)
			"actuator_pompa_utama_1" -> _editedActuatorStatus.value.copy(actuator_pompa_utama_1 = value)
			"actuator_pompa_utama_2" -> _editedActuatorStatus.value.copy(actuator_pompa_utama_2 = value)
			else -> _editedActuatorStatus.value
		}
	}

	fun confirmEdit() {
		viewModelScope.launch {
			val result = publishActuatorStatusUseCase(_editedActuatorStatus.value)
			if (result.isSuccess) {
				_actuatorStatus.value = _editedActuatorStatus.value

				// Simpan status terbaru ke DataStore
				actuatorPreferencesManager.saveActuatorStatus(_editedActuatorStatus.value)
				Log.d("ActuatorViewModel", "Actuator status updated successfully")
			} else {
				Log.e("ActuatorViewModel", "Failed to update actuator status")
			}
		}
	}


	private fun parseActuatorMessage(message: String): ActuatorStatus {
		val jsonObject = JSONObject(message)
		return ActuatorStatus(
			actuator_nutrisi = jsonObject.optInt("actuator_nutrisi"),
			actuator_ph_up = jsonObject.optInt("actuator_ph_up"),
			actuator_ph_down = jsonObject.optInt("actuator_ph_down"),
			actuator_air_baku = jsonObject.optInt("actuator_air_baku"),
			actuator_pompa_utama_1 = jsonObject.optInt("actuator_pompa_utama_1"),
			actuator_pompa_utama_2 = jsonObject.optInt("actuator_pompa_utama_2")
		)
	}
}



