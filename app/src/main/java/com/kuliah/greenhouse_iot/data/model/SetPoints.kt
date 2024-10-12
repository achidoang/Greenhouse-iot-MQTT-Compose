package com.kuliah.greenhouse_iot.data.model

data class Setpoints(
	val waterTemp: Int = 0,
	val waterPh: Double = 7.9,
	val waterPpm: Int = 3000,
	val airTemp: Int = 25,
	val airHum: Int = 50
)
