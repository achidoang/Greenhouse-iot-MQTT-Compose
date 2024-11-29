package com.kuliah.greenhouse_iot.presentation.viewmodel.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.data.model.history.TimeType
import com.kuliah.greenhouse_iot.domain.usecases.history.GetDailyAveragesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.history.GetMonthlyAveragesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.history.GetWeeklyAveragesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
	private val getDailyAveragesUseCase: GetDailyAveragesUseCase,
	private val getWeeklyAveragesUseCase: GetWeeklyAveragesUseCase,
) : ViewModel() {

	private val _dailyAverages = MutableStateFlow<List<AverageHistory>?>(null)
	val dailyAverages: StateFlow<List<AverageHistory>?> = _dailyAverages

	private val _weeklyAverages = MutableStateFlow<List<AverageHistory>?>(null)
	val weeklyAverages: StateFlow<List<AverageHistory>?> = _weeklyAverages

	private val _monthlyAverages = MutableStateFlow<List<AverageHistory>?>(null)
	val monthlyAverages: StateFlow<List<AverageHistory>?> = _monthlyAverages

	private val _error = MutableStateFlow<String?>(null)
	val error: StateFlow<String?> = _error

	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading


	init {
		fetchAllData()
	}

//	private fun fetchAllData() {
//		fetchDailyAverages()
//		fetchWeeklyAverages()
//	}

	private fun fetchAllData() {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				_dailyAverages.value = getDailyAveragesUseCase()
				_weeklyAverages.value = getWeeklyAveragesUseCase()
			} catch (e: Exception) {
				_error.value = "Failed to fetch data: ${e.message}"
			} finally {
				_isLoading.value = false
			}
		}
	}

//	private fun fetchDailyAverages() {
//		viewModelScope.launch {
//			try {
//				_dailyAverages.value = getDailyAveragesUseCase()
//			} catch (e: Exception) {
//				_error.value = "Failed to fetch daily data: ${e.message}"
//			}
//		}
//	}
//
//	private fun fetchWeeklyAverages() {
//		viewModelScope.launch {
//			try {
//				_weeklyAverages.value = getWeeklyAveragesUseCase()
//			} catch (e: Exception) {
//				_error.value = "Failed to fetch weekly data: ${e.message}"
//			}
//		}
//	}

}
