package com.kuliah.greenhouse_iot.data.model

data class SetPointData(
	val timestamp: String,
//	val id: Int,
	val watertemp: Float,
	val waterppm: Float,
	val waterph: Float,
	val profile: String
)