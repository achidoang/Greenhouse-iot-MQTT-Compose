package com.kuliah.greenhouse_iot.data.model.subscribe


data class MonitoringData(
	val watertemp: Float,
	val waterppm: Float,
	val waterph: Float,
	val airtemp: Float,
	val airhum: Float,
	val timestamp: String
)