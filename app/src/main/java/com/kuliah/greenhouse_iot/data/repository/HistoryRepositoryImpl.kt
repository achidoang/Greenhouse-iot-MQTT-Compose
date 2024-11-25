package com.kuliah.greenhouse_iot.data.repository

import android.annotation.SuppressLint
import com.kuliah.greenhouse_iot.data.local.datastore.DataStoreManager
import com.kuliah.greenhouse_iot.data.model.subscribe.AktuatorData
import com.kuliah.greenhouse_iot.data.model.subscribe.MonitoringData
import com.kuliah.greenhouse_iot.data.model.subscribe.SetPointData
import com.kuliah.greenhouse_iot.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
	private val dataStoreManager: DataStoreManager
) : HistoryRepository {

	@SuppressLint("NewApi")
	override fun getMonitoringHistory(): Flow<List<MonitoringData>> {
		return dataStoreManager.getMonitoringDataHistory()
	}

	@SuppressLint("NewApi")
	override fun getAktuatorHistory(): Flow<List<AktuatorData>> {
		return dataStoreManager.getAktuatorDataHistory()
	}

	@SuppressLint("NewApi")
	override fun getSetPointHistory(): Flow<List<SetPointData>> {
		return dataStoreManager.getSetPointDataHistory()
	}
}
