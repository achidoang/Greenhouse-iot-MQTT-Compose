package com.kuliah.greenhouse_iot.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class MonitoringRepositoryImpl @Inject constructor(
	private val mqttClientService: MqttClientService,
	@ApplicationContext private val context: Context
) : MonitoringRepository {

	private val _monitoringData = MutableStateFlow<MonitoringData?>(null)
	private val _errorState = MutableStateFlow<String?>(null)
	private val prefs = context.getSharedPreferences("MonitoringPrefs", Context.MODE_PRIVATE)


	override fun subscribeToMonitoringData(): StateFlow<MonitoringData?> = _monitoringData
	override fun getErrorState(): StateFlow<String?> = _errorState

	init {
		// Load saved data on initialization
		val savedDataJson = prefs.getString("lastMonitoringData", null)
		savedDataJson?.let {
			_monitoringData.value = Gson().fromJson(it, MonitoringData::class.java)
		}
	}

	fun startListening() {
		Log.d("MonitoringRepository", "Starting to listen for MQTT data")
		mqttClientService.connect(
			onConnected = {
				Log.d("MonitoringRepository", "Connected to MQTT, subscribing to topic")
				mqttClientService.subscribe("herbalawu/monitoring", 0) { message ->
					try {
						val data = Gson().fromJson(message, MonitoringData::class.java)
						_monitoringData.value = data

						// Save the data locally
						val editor = prefs.edit()
						editor.putString("lastMonitoringData", Gson().toJson(data))
						editor.apply()
					} catch (e: Exception) {
						// Abaikan kesalahan parsing dan jangan mengupdate _errorState
						Log.e("MonitoringRepository", "Failed to parse monitoring data, ignoring: ${e.message}")
						// Tidak perlu mengubah UI atau memberikan notifikasi
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
