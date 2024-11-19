package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.data.model.history.MonitoringHistory
import com.kuliah.greenhouse_iot.data.model.history.PaginatedResponse

interface MonitoringRepository {
	suspend fun getHistoryPaginated(
		page: Int,
		limit: Int,
		startDate: String? = null,
		endDate: String? = null,
		sortBy: String? = "timestamp",
		order: String? = "DESC"
	): PaginatedResponse<MonitoringHistory>

	suspend fun getDailyAverages(): List<AverageHistory>
	suspend fun getWeeklyAverages(): List<AverageHistory>
	suspend fun getMonthlyAverages(): List<AverageHistory>
}