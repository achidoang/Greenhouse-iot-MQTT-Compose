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
import org.json.JSONObject
import javax.inject.Inject


class MonitoringRepositoryImpl @Inject constructor(
	private val mqttClientService: MqttClientService
) : MonitoringRepository {

	override fun subscribeToMonitoringTopic(onDataReceived: (MonitoringData) -> Unit, onError: (Throwable) -> Unit) {
		mqttClientService.subscribe("herbalawu/monitoring", 1) { message ->
			try {
				val data = parseMonitoringData(message)
				onDataReceived(data)
			} catch (e: Exception) {
				onError(e)
			}
		}
	}

	private fun parseMonitoringData(message: String): MonitoringData {
		val json = JSONObject(message)
		return MonitoringData(
			watertemp = json.getDouble("watertemp"),
			waterppm = json.getDouble("waterppm"),
			waterph = json.getDouble("waterph"),
			airtemp = json.getDouble("airtemp"),
			airhum = json.getDouble("airhum")
		)
	}
}
