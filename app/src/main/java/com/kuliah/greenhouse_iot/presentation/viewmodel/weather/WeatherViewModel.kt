package com.kuliah.greenhouse_iot.presentation.viewmodel.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.local.datastore.WeatherDataStore
import com.kuliah.greenhouse_iot.data.model.location.LocationRequest
import com.kuliah.greenhouse_iot.data.model.weather.ForecastResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import com.kuliah.greenhouse_iot.domain.usecases.location.PostLocationUseCase
import com.kuliah.greenhouse_iot.domain.usecases.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
	private val getWeatherUseCase: GetWeatherUseCase,
	private val postLocationUseCase: PostLocationUseCase,
	private val weatherDataStore: WeatherDataStore
) : ViewModel() {

	private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
	val weatherState: StateFlow<WeatherResponse?> = _weatherState

	private val _locationState = MutableStateFlow<String?>(null)
	val locationState: StateFlow<String?> = _locationState

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

	fun fetchWeather() {
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

	fun postLocation(latitude: Double, longitude: Double) {
		viewModelScope.launch {
			try {
				val locationRequest = LocationRequest(latitude, longitude)
				val response = postLocationUseCase(locationRequest)
				_locationState.value = response.message
			} catch (e: Exception) {
				Log.e("WeatherViewModel", "Error posting location: ${e.message}")
			}
		}
	}
}
