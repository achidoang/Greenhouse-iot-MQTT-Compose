package com.kuliah.greenhouse_iot.presentation.viewmodel.weather

import android.util.Log
import androidx.work.Constraints
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kuliah.greenhouse_iot.data.local.datastore.WeatherDataStore
import com.kuliah.greenhouse_iot.data.model.location.LocationRequest
import com.kuliah.greenhouse_iot.data.model.weather.ForecastResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import com.kuliah.greenhouse_iot.domain.usecases.location.PostLocationUseCase
import com.kuliah.greenhouse_iot.domain.usecases.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
	private val getWeatherUseCase: GetWeatherUseCase,
	private val postLocationUseCase: PostLocationUseCase,
	private val weatherDataStore: WeatherDataStore,
	private val workManager: WorkManager
) : ViewModel() {

	private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
	val weatherState: StateFlow<WeatherResponse?> = _weatherState

	private val _locationState = MutableStateFlow<String?>(null)
	val locationState: StateFlow<String?> = _locationState

	private var updateJob: Job? = null

	init {
		loadWeatherFromDataStore()
	}

	private fun loadWeatherFromDataStore() {
		viewModelScope.launch {
			weatherDataStore.getWeather().collect { weather ->
				_weatherState.value = weather
			}
		}
	}

	fun startPeriodicWeatherUpdates() {
		updateJob?.cancel()
		updateJob = viewModelScope.launch {
			while (isActive) {
				fetchWeather()
				delay(10 * 60 * 1000) // 10 minutes in milliseconds
			}
		}
	}

	fun stopPeriodicWeatherUpdates() {
		updateJob?.cancel()
		updateJob = null
	}

	private fun fetchWeather() {
		viewModelScope.launch {
			try {
				val weather = getWeatherUseCase()
				_weatherState.value = weather
				saveWeatherToDataStore(weather)
			} catch (e: Exception) {
				Log.e("WeatherViewModel", "Error fetching weather: ${e.message}")
			}
		}
	}

	private fun saveWeatherToDataStore(weather: WeatherResponse) {
		viewModelScope.launch {
			weatherDataStore.saveWeather(weather)
		}
	}

	fun schedulePeriodicWeatherUpdates() {
		val constraints = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.build()

		val updateRequest = PeriodicWorkRequestBuilder<WeatherUpdateWorker>(1, TimeUnit.MINUTES)
			.setConstraints(constraints)
			.build()

		workManager.enqueueUniquePeriodicWork(
			"weather_update",
			ExistingPeriodicWorkPolicy.REPLACE,
			updateRequest
		)
	}

	fun postLocation(latitude: Double, longitude: Double) {
		viewModelScope.launch {
			try {
				val locationRequest = LocationRequest(latitude, longitude)
				val response = postLocationUseCase(locationRequest)
				_locationState.value = response.message
				fetchWeather() // Fetch weather immediately
				schedulePeriodicWeatherUpdates() // Schedule periodic updates
				startPeriodicWeatherUpdates() // Start in-app updates
			} catch (e: Exception) {
				Log.e("WeatherViewModel", "Error posting location: ${e.message}")
			}
		}
	}

	fun isWeatherDataStale(): Boolean {
		val currentTime = System.currentTimeMillis()
		val lastUpdateTime = _weatherState.value?.data?.list?.get(0)?.dt?.times(1000) ?: 0
		return (currentTime - lastUpdateTime) > 1 * 60 * 1000 // 10 minutes in milliseconds
	}

	override fun onCleared() {
		super.onCleared()
		stopPeriodicWeatherUpdates()
	}
}

