package com.kuliah.greenhouse_iot.domain.usecases.history

import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import javax.inject.Inject

class GetDailyAveragesUseCase @Inject constructor(
	private val repository: MonitoringRepository
) {
	suspend operator fun invoke() = repository.getDailyAverages()
}