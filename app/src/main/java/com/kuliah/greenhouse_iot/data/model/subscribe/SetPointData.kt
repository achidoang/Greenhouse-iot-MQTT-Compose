package com.kuliah.greenhouse_iot.data.model.subscribe

data class SetPointData(
	val timestamp: String,
	val watertemp: Float,
	val waterppm: Float,
	val waterph: Float,
	val status: String,
	val profile: String
)