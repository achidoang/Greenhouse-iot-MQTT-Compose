package com.kuliah.greenhouse_iot.presentation.viewmodel.chart

import android.annotation.SuppressLint
import android.util.Log
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
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale
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

	private val _error = MutableStateFlow<String?>(null)
	val error: StateFlow<String?> = _error

	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading

	private val _dailyXAxisLabels = MutableStateFlow<List<String>>(emptyList())
	val dailyXAxisLabels: StateFlow<List<String>> = _dailyXAxisLabels

	private val _weeklyXAxisLabels = MutableStateFlow<List<String>>(emptyList())
	val weeklyXAxisLabels: StateFlow<List<String>> = _weeklyXAxisLabels


	init {
		fetchAllData()
	}

	private fun fetchAllData() {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val dailyData = getDailyAveragesUseCase()
				val weeklyData = getWeeklyAveragesUseCase()

				// Proses data harian
				val (processedDailyData, dailyLabels) = processDailyData(dailyData)
				_dailyAverages.value = processedDailyData
				_dailyXAxisLabels.value = dailyLabels

				// Proses data mingguan
				val (processedWeeklyData, weeklyLabels) = processWeeklyData(weeklyData)
				_weeklyAverages.value = processedWeeklyData
				_weeklyXAxisLabels.value = weeklyLabels
			} catch (e: Exception) {
				_error.value = "Failed to fetch data: ${e.message}"
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun processDailyData(data: List<AverageHistory>): Pair<List<AverageHistory>, List<String>> {
		val calendar = Calendar.getInstance()
		val today = calendar.time
		calendar.add(Calendar.DAY_OF_MONTH, -6) // Memulai dari 7 hari terakhir

		val xAxisLabels = mutableListOf<String>()
		val filteredData = mutableListOf<AverageHistory>()

		for (i in 0..6) {
			val currentDate = calendar.time
			val dateLabel = SimpleDateFormat("dd, MMM", Locale("id")).format(currentDate)

			xAxisLabels.add(dateLabel)

			// Filter data berdasarkan tanggal
			data.find {
				it.day == calendar.get(Calendar.DAY_OF_MONTH) && it.month == calendar.get(Calendar.MONTH) + 1
			}?.let {
				filteredData.add(it)
			}

			// Pindah ke hari berikutnya
			calendar.add(Calendar.DAY_OF_MONTH, 1)
		}

		return Pair(filteredData, xAxisLabels)
	}

	fun processWeeklyData(data: List<AverageHistory>): Pair<List<AverageHistory>, List<String>> {
		val calendar = Calendar.getInstance()
		val xAxisLabels = mutableListOf<String>()
		val filteredData = mutableListOf<AverageHistory>()

		// Ambil minggu saat ini
		val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
		val currentYear = calendar.get(Calendar.YEAR)

		// Iterasi untuk mendapatkan 5 minggu terakhir
		for (weekOffset in 0 until 5) {
			val week = currentWeek - weekOffset

			// Sesuaikan jika minggu berada di tahun sebelumnya
			if (week <= 0) {
				calendar.set(Calendar.YEAR, currentYear - 1)
				calendar.set(Calendar.WEEK_OF_YEAR, week + 52) // Asumsi 52 minggu dalam setahun
			} else {
				calendar.set(Calendar.YEAR, currentYear)
				calendar.set(Calendar.WEEK_OF_YEAR, week)
			}

			// Format label: minggu X, bulan
			val weekLabel = "week ${calendar.get(Calendar.WEEK_OF_MONTH)}, ${
				SimpleDateFormat("MMM", Locale("id")).format(calendar.time)
			}"
			xAxisLabels.add(0, weekLabel) // Tambahkan di awal untuk memastikan urutan

			// Filter data berdasarkan minggu dan tahun
			val weekData = data.filter {
				it.week == calendar.get(Calendar.WEEK_OF_YEAR) &&
						it.year == calendar.get(Calendar.YEAR)
			}

			// Tambahkan data hanya jika tersedia
			if (weekData.isNotEmpty()) {
				filteredData.addAll(weekData)
			}
		}

		return Pair(filteredData, xAxisLabels)
	}

}




