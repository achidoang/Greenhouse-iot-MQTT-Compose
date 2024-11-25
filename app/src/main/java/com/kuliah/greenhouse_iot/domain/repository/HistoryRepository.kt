package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.subscribe.AktuatorData
import com.kuliah.greenhouse_iot.data.model.subscribe.MonitoringData
import com.kuliah.greenhouse_iot.data.model.subscribe.SetPointData
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
	fun getMonitoringHistory(): Flow<List<MonitoringData>>
	fun getAktuatorHistory(): Flow<List<AktuatorData>>
	fun getSetPointHistory(): Flow<List<SetPointData>>
}
