package com.kuliah.greenhouse_iot.data.model.subscribe


data class MonitoringData(
	val watertemp: Float = Float.NaN,
	val waterppm: Float = Float.NaN,
	val waterph: Float = Float.NaN,
	val airtemp: Float = Float.NaN, // Default NaN untuk nilai tidak valid
	val airhum: Float = Float.NaN, // Default NaN untuk nilai tidak valid
	val timestamp: String
)
