package com.kuliah.greenhouse_iot.domain.usecases.history

import com.kuliah.greenhouse_iot.data.model.AktuatorData
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.model.SetPointData
import com.kuliah.greenhouse_iot.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
	private val repository: HistoryRepository
) {
	fun getMonitoringHistory(): Flow<List<MonitoringData>> = repository.getMonitoringHistory()
	fun getAktuatorHistory(): Flow<List<AktuatorData>> = repository.getAktuatorHistory()
	fun getSetPointHistory(): Flow<List<SetPointData>> = repository.getSetPointHistory()
}
