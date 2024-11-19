package com.kuliah.greenhouse_iot.data.remote.api.history

import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.data.model.history.MonitoringHistory
import com.kuliah.greenhouse_iot.data.model.history.PaginatedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MonitoringApi {
	@GET("history/monitoring/paginated")
	suspend fun getHistoryPaginated(
		@Query("page") page: Int,
		@Query("limit") limit: Int,
		@Query("startDate") startDate: String? = null,
		@Query("endDate") endDate: String? = null,
		@Query("sortBy") sortBy: String? = "timestamp",
		@Query("order") order: String? = "DESC"
	): PaginatedResponse<MonitoringHistory>

	@GET("history/monitoring/daily")
	suspend fun getDailyAverages(): List<AverageHistory>

	@GET("history/monitoring/weekly")
	suspend fun getWeeklyAverages(): List<AverageHistory>

	@GET("history/monitoring/monthly")
	suspend fun getMonthlyAverages(): List<AverageHistory>
}
