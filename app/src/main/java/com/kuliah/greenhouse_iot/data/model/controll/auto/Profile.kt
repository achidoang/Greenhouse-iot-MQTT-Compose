package com.kuliah.greenhouse_iot.data.model.controll.auto

data class Profile(
	val id: Int,
	val watertemp: Double,
	val waterppm: Double,
	val waterph: Double,
	val profile: String,
	val status: Int,
	val timestamp: String,
	val order: Int = 0
)
