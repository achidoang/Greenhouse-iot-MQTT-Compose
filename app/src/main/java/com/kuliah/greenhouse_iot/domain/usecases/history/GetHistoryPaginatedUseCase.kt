package com.kuliah.greenhouse_iot.domain.usecases.history

import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import javax.inject.Inject

class GetHistoryPaginatedUseCase @Inject constructor(
	private val repository: MonitoringRepository
) {
	suspend operator fun invoke(
		page: Int, limit: Int, startDate: String?, endDate: String?, sortBy: String?, order: String?
	) = repository.getHistoryPaginated(page, limit, startDate, endDate, sortBy, order)
}