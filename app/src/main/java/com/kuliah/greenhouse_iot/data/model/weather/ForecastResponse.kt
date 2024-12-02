package com.kuliah.greenhouse_iot.data.model.weather

data class ForecastResponse(
	val date: String, // e.g., "2024-12-03"
	val temperature: Double,
	val condition: String // e.g., "Sunny"
)