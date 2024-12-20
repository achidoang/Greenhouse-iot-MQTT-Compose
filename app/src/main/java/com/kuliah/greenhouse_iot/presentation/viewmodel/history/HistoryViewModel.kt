package com.kuliah.greenhouse_iot.presentation.viewmodel.history

import android.annotation.SuppressLint
import android.util.Log
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
	private val getHistoryPaginatedUseCase: GetHistoryPaginatedUseCase
) : ViewModel() {

	private val _uiState = MutableStateFlow(HistoryUiState())
	val uiState: StateFlow<HistoryUiState> = _uiState

	@SuppressLint("NewApi")
	fun loadHistory() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			try {
				Log.d("HistoryViewModel", "Start Date: ${_uiState.value.startDate}, End Date: ${_uiState.value.endDate}")
				val result = getHistoryPaginatedUseCase(
					_uiState.value.currentPage,
					_uiState.value.itemsPerPage,
					_uiState.value.startDate?.format(DateTimeFormatter.ISO_DATE),
					_uiState.value.endDate?.format(DateTimeFormatter.ISO_DATE),
					_uiState.value.sortBy,
					_uiState.value.order
				)
				_uiState.update {
					Log.d("HistoryViewModel", "Total Pages: ${result.totalPages}")
					it.copy(
						history = result.data,
						totalPages = result.totalPages,
						isLoading = false
					)
				}
			} catch (e: Exception) {
				if (e is retrofit2.HttpException) {
					val errorBody = e.response()?.errorBody()?.string()
					Log.e("HistoryViewModel", "HttpException: ${e.code()} - $errorBody", e)
				} else {
					Log.e("HistoryViewModel", "Error loading history", e)
				}
				_uiState.update { it.copy(isLoading = false) }
			}
		}
	}


	fun updatePage(newPage: Int) {
		_uiState.update { it.copy(currentPage = newPage) }
	}

	fun updateStartDate(date: LocalDate?) {
		_uiState.update { it.copy(startDate = date) }
	}

	fun updateEndDate(date: LocalDate?) {
		_uiState.update { it.copy(endDate = date) }
	}

	fun updateSorting(sortBy: String, order: String) {
		_uiState.update { it.copy(sortBy = sortBy, order = order) }
	}
}

data class HistoryUiState(
	val history: List<MonitoringHistory> = emptyList(),
	val isLoading: Boolean = false,
	val currentPage: Int = 1,
	val itemsPerPage: Int = 8,
	val totalPages: Int = 50,
	val startDate: LocalDate? = null,
	val endDate: LocalDate? = null,
	val sortBy: String = "timestamp",
	val order: String = "DESC"
)



