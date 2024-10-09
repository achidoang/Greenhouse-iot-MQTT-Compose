package com.kuliah.greenhouse_iot.data.repository

import android.util.Log
import com.google.gson.Gson
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class MonitoringRepositoryImpl @Inject constructor(
	private val mqttClientService: MqttClientService
) : MonitoringRepository {

	private val _monitoringData = MutableStateFlow<MonitoringData?>(null)
	private val _errorState = MutableStateFlow<String?>(null)

	override fun subscribeToMonitoringData(): StateFlow<MonitoringData?> = _monitoringData
	override fun getErrorState(): StateFlow<String?> = _errorState

	fun startListening() {
		Log.d("MonitoringRepository", "Starting to listen for MQTT data")
		mqttClientService.connect(
			onConnected = {
				Log.d("MonitoringRepository", "Connected to MQTT, subscribing to topic")
				mqttClientService.subscribe("herbalawu/monitoring", 0) { message ->
					try {
						Log.d("MonitoringRepository", "Received message: $message")
						val data = Gson().fromJson(message, MonitoringData::class.java)
						_monitoringData.value = data
					} catch (e: Exception) {
						Log.e("MonitoringRepository", "Failed to parse monitoring data: ${e.message}")
						_errorState.value = "Error parsing data"
					}
				}
			},
			onError = { exception ->
				Log.e("MonitoringRepository", "Failed to connect to MQTT broker: ${exception?.message}")
				_errorState.value = exception?.message ?: "Failed to connect to MQTT broker"
			}
		)
	}
}
