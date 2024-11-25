package com.kuliah.greenhouse_iot.presentation.viewmodel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.data.model.history.MonitoringHistory
import com.kuliah.greenhouse_iot.data.model.history.TimeType
import com.kuliah.greenhouse_iot.domain.usecases.history.GetDailyAveragesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.history.GetHistoryPaginatedUseCase
import com.kuliah.greenhouse_iot.domain.usecases.history.GetMonthlyAveragesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.history.GetWeeklyAveragesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
	private val getHistoryPaginatedUseCase: GetHistoryPaginatedUseCase,
	private val getDailyAveragesUseCase: GetDailyAveragesUseCase,
	private val getWeeklyAveragesUseCase: GetWeeklyAveragesUseCase,
	private val getMonthlyAveragesUseCase: GetMonthlyAveragesUseCase
) : ViewModel() {

	private val _history = MutableStateFlow<List<MonitoringHistory>>(emptyList())
	val history: StateFlow<List<MonitoringHistory>> = _history

	private val _averages = MutableStateFlow<List<AverageHistory>>(emptyList())
	val averages: StateFlow<List<AverageHistory>> = _averages

	private val _isLoading = MutableStateFlow(false)
	val isLoading: StateFlow<Boolean> = _isLoading

	fun loadHistoryPaginated(page: Int, limit: Int, startDate: String?, endDate: String?, sortBy: String?, order: String?) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val result = getHistoryPaginatedUseCase(page, limit, startDate, endDate, sortBy, order)
				_history.value = result.data
			} catch (e: Exception) {
				// Tangani error
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun loadAverages(type: TimeType) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val result = when (type) {
					TimeType.DAILY -> getDailyAveragesUseCase()
					TimeType.WEEKLY -> getWeeklyAveragesUseCase()
					TimeType.MONTHLY -> getMonthlyAveragesUseCase()
				}
				_averages.value = result
			} catch (e: Exception) {
				// Tangani error
			} finally {
				_isLoading.value = false
			}
		}
	}
}


