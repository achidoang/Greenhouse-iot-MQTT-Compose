package com.kuliah.greenhouse_iot.data.model.history

data class MonitoringHistory(
	val id: Int,
	val watertemp: Float,
	val waterppm: Float,
	val waterph: Float,
	val airtemp: Float,
	val airhum: Float,
	val timestamp: String
)