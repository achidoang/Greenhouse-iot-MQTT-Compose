package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MonitoringRepository {
	fun subscribeToMonitoringData(): StateFlow<MonitoringData?>
	fun getErrorState(): StateFlow<String?> // Tambahkan ini
}

