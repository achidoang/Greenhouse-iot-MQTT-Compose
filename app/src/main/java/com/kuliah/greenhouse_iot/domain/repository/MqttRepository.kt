package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.AktuatorData
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.model.SetPointData
import com.kuliah.greenhouse_iot.util.ConnectionStatus
import kotlinx.coroutines.flow.Flow


interface MqttRepository {
	// Fungsi untuk subscribe ke topik monitoring
	fun subscribeMonitoring(): Flow<MonitoringData>

	// Fungsi untuk subscribe ke topik aktuator
	fun subscribeAktuator(): Flow<AktuatorData>

	// Fungsi untuk subscribe ke topik setpoint
	fun subscribeSetPoint(): Flow<SetPointData>

	// Fungsi untuk memeriksa status koneksi WebSocket
	fun getConnectionStatus(): Flow<ConnectionStatus>
	suspend fun reconnect()


}
