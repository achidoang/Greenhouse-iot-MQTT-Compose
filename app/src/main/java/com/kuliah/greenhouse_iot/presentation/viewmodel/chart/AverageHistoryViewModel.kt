package com.kuliah.greenhouse_iot.presentation.viewmodel.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.domain.usecases.history.GetDailyAveragesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.history.GetMonthlyAveragesUseCase
import com.kuliah.greenhouse_iot.domain.usecases.history.GetWeeklyAveragesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AverageHistoryViewModel @Inject constructor(
	private val getDailyAveragesUseCase: GetDailyAveragesUseCase,
	private val getWeeklyAveragesUseCase: GetWeeklyAveragesUseCase,
	private val getMonthlyAveragesUseCase: GetMonthlyAveragesUseCase
) : ViewModel() {

	private val _dailyData = MutableStateFlow<List<AverageHistory>>(emptyList())
	val dailyData: StateFlow<List<AverageHistory>> = _dailyData

	private val _weeklyData = MutableStateFlow<List<AverageHistory>>(emptyList())
	val weeklyData: StateFlow<List<AverageHistory>> = _weeklyData

	private val _monthlyData = MutableStateFlow<List<AverageHistory>>(emptyList())
	val monthlyData: StateFlow<List<AverageHistory>> = _monthlyData

	private val _isLoading = MutableStateFlow(false)
	val isLoading: StateFlow<Boolean> = _isLoading

	fun loadDailyData() {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				_dailyData.value = getDailyAveragesUseCase()
			} catch (e: Exception) {
				// Handle error
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun loadWeeklyData() {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				_weeklyData.value = getWeeklyAveragesUseCase()
			} catch (e: Exception) {
				// Handle error
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun loadMonthlyData() {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				_monthlyData.value = getMonthlyAveragesUseCase()
			} catch (e: Exception) {
				// Handle error
			} finally {
				_isLoading.value = false
			}
		}
	}
}
