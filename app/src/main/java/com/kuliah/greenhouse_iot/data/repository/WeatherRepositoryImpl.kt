package com.kuliah.greenhouse_iot.data.repository

import com.kuliah.greenhouse_iot.data.model.location.LocationRequest
import com.kuliah.greenhouse_iot.data.model.location.LocationResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherForecastResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import com.kuliah.greenhouse_iot.data.remote.api.weather.WeatherApi
import com.kuliah.greenhouse_iot.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl(private val api: WeatherApi) : WeatherRepository {
	override suspend fun getWeather(): WeatherResponse {
		val response = api.getWeather()
		if (response.isSuccessful) {
			return response.body()!!
		} else {
			throw Exception("Error fetching weather: ${response.errorBody()?.string()}")
		}
	}

	override suspend fun postLocation(locationRequest: LocationRequest): LocationResponse {
		val response = api.postLocation(locationRequest)
		if (response.isSuccessful) {
			return response.body()!!
		} else {
			throw Exception("Error posting location: ${response.errorBody()?.string()}")
		}
	}
}
