package com.kuliah.greenhouse_iot.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MqttRepository {
	suspend fun connect()
	suspend fun disconnect()
	suspend fun subscribe(topic: String)
	suspend fun publish(topic: String, message: String)
	suspend fun reconnect()
	fun observeMessages(): StateFlow<String?>
	fun observeConnectionStatus(): StateFlow<Boolean>
}
