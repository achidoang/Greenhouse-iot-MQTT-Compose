package com.kuliah.greenhouse_iot.presentation.viewmodel.setPoint

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.local.SetpointDataStoreManager
import com.kuliah.greenhouse_iot.data.model.Setpoints
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.domain.usecases.controll_mqtt.PublishSetpointsUseCase
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.GetSetpointsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SetpointViewModel @Inject constructor(
	private val getSetpointsUseCase: GetSetpointsUseCase,
	private val publishSetpointsUseCase: PublishSetpointsUseCase,
	private val mqttClientService: MqttClientService,
	private val setpointDataStoreManager: SetpointDataStoreManager
) : ViewModel() {

	private val _setpoints = MutableStateFlow(Setpoints())
	val setpoints: StateFlow<Setpoints> = _setpoints

	private val _editedSetpoints = MutableStateFlow(Setpoints())
	val editedSetpoints: StateFlow<Setpoints> = _editedSetpoints

	private var retryCount = 0
	private val maxRetries = 3

	init {
		viewModelScope.launch {
			setpointDataStoreManager.getSetpoint().collect { savedSetpoint ->
				_setpoints.value = savedSetpoint
				_editedSetpoints.value = savedSetpoint
			}
		}
		// Fetch setpoints on ViewModel initialization and subscribe to MQTT topic
		subscribeToSetpointUpdates()
	}

	private fun subscribeToSetpointUpdates() {
		mqttClientService.subscribe("herbalawu/setpoint", 1) { message ->
			// Parse the message and update the setpoints accordingly
			try {
				val updatedSetpoints = parseSetpointMessage(message)
				_setpoints.value = updatedSetpoints
				_editedSetpoints.value = updatedSetpoints

				viewModelScope.launch {
					setpointDataStoreManager.saveSetpoints(updatedSetpoints)
				}
			} catch (e: Exception) {
				// If message is invalid or not in correct format, ignore it
				Log.e("SetpointViewModel", "Invalid MQTT message: ${e.message}")
			}
		}
	}

	fun startEdit() {
		_editedSetpoints.value = _setpoints.value
	}

	// Handle setpoint changes from UI input
	fun onSetpointChanged(waterTemp: Float, waterPpm: Float, waterPh: Float, airTemp: Float, airHum: Float) {
		_editedSetpoints.value = _editedSetpoints.value.copy(
			waterTemp = waterTemp.toInt(),
			waterPpm = waterPpm.toInt(),
			waterPh = waterPh.toDouble(),
			airTemp = airTemp.toInt(),
			airHum = airHum.toInt()
		)
	}

	// Confirm the setpoint edits and publish to MQTT
	fun confirmEdit() {
		tryPublishSetpoints()
	}
	private fun saveSetpointsToDataStore(setpoints: Setpoints) {
		viewModelScope.launch {
			setpointDataStoreManager.saveSetpoints(setpoints)
		}
	}

	private fun tryPublishSetpoints() {
		viewModelScope.launch {
			val editedSetpoint = _editedSetpoints.value
			val result = publishSetpointsUseCase(_editedSetpoints.value)
			if (result.isSuccess) {
				retryCount = 0
				_setpoints.value = editedSetpoint
				saveSetpointsToDataStore(editedSetpoint)
				Log.d("SetpointViewModel", "Setpoints updated successfully")
				// Publish updated setpoints to MQTT
				publishSetpointsToMqtt(_editedSetpoints.value)
			} else {
				retryCount++
				if (retryCount <= maxRetries) {
					delay(5000) // Retry after 5 seconds
					tryPublishSetpoints()
				} else {
					Log.e("SetpointViewModel", "Failed to update setpoints after retries")
				}
			}
		}
	}

	// Publish setpoints to MQTT topic
	private fun publishSetpointsToMqtt(setpoints: Setpoints) {
		val message = JSONObject().apply {
			put("watertemp", setpoints.waterTemp)
			put("waterppm", setpoints.waterPpm)
			put("waterph", setpoints.waterPh)
			put("airtemp", setpoints.airTemp)
			put("airhum", setpoints.airHum)
		}.toString()

		mqttClientService.publish("herbalawu/setpoint", message)
	}

	// Function to parse the JSON message into a Setpoints object
	private fun parseSetpointMessage(message: String): Setpoints {
		val jsonObject = JSONObject(message)
		return Setpoints(
			waterTemp = jsonObject.optInt("watertemp"),
			waterPpm = jsonObject.optInt("waterppm"),
			waterPh = jsonObject.optDouble("waterph"),
			airTemp = jsonObject.optInt("airtemp"),
			airHum = jsonObject.optInt("airhum")
		)
	}
}
