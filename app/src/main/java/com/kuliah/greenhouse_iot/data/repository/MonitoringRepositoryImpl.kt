package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.data.model.history.MonitoringHistory
import com.kuliah.greenhouse_iot.data.model.history.PaginatedResponse
import com.kuliah.greenhouse_iot.data.model.history.TimeType
import com.kuliah.greenhouse_iot.data.remote.api.history.MonitoringApi
import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import javax.inject.Inject

class MonitoringRepositoryImpl @Inject constructor(
	private val api: MonitoringApi
) : MonitoringRepository {
	override suspend fun getHistoryPaginated(
		page: Int, limit: Int, startDate: String?, endDate: String?, sortBy: String?, order: String?
	): PaginatedResponse<MonitoringHistory> {
		return api.getHistoryPaginated(page, limit, startDate, endDate, sortBy, order)
	}

	override suspend fun getDailyAverages(): List<AverageHistory> {
		return api.getDailyAverages().map { it.copy(timeType = TimeType.DAILY) }
	}

	override suspend fun getWeeklyAverages(): List<AverageHistory> {
		return api.getWeeklyAverages().map { it.copy(timeType = TimeType.WEEKLY) }
	}

	override suspend fun getMonthlyAverages(): List<AverageHistory> {
		return api.getMonthlyAverages().map { it.copy(timeType = TimeType.MONTHLY) }
	}
}
