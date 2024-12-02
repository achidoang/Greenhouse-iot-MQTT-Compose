package com.kuliah.greenhouse_iot.data.model.weather

data class WeatherForecastResponse(
	val current: WeatherResponse,
	val forecast: List<ForecastResponse>
)