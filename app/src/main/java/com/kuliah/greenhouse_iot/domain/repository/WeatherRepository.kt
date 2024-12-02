package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.location.LocationRequest
import com.kuliah.greenhouse_iot.data.model.location.LocationResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherForecastResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse

interface WeatherRepository {
	suspend fun getWeather(): WeatherResponse
	suspend fun postLocation(locationRequest: LocationRequest): LocationResponse
}
