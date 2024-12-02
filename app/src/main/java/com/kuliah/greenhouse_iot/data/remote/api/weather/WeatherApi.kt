package com.kuliah.greenhouse_iot.data.remote.api.weather

import com.kuliah.greenhouse_iot.data.model.location.LocationRequest
import com.kuliah.greenhouse_iot.data.model.location.LocationResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherForecastResponse
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST



interface WeatherApi {
	@GET("api/v2/weather")
	suspend fun getWeather(): Response<WeatherResponse>

	@POST("api/v2/location")
	suspend fun postLocation(@Body locationRequest: LocationRequest): Response<LocationResponse>
}
