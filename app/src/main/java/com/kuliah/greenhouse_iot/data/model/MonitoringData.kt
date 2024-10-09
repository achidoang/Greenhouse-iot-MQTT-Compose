package com.kuliah.greenhouse_iot.data.model

data class MonitoringData(
	val watertemp: Double,
	val waterppm: Double,
	val waterph: Double,
	val airtemp: Double,
	val airhum: Double
)

