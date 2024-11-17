package com.kuliah.greenhouse_iot.domain.repository

import android.annotation.SuppressLint
import com.kuliah.greenhouse_iot.data.local.datastore.DataStoreManager
import com.kuliah.greenhouse_iot.data.model.AktuatorData
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.model.SetPointData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface HistoryRepository {
	fun getMonitoringHistory(): Flow<List<MonitoringData>>
	fun getAktuatorHistory(): Flow<List<AktuatorData>>
	fun getSetPointHistory(): Flow<List<SetPointData>>
}
