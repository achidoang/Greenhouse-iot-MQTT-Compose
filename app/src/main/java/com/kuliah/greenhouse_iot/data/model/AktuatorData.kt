package com.kuliah.greenhouse_iot.data.model

data class AktuatorData(
	val timestamp: String,
//	val id: Int,
	val actuator_nutrisi: Boolean,
	val actuator_ph_up: Boolean,
	val actuator_ph_down: Boolean,
	val actuator_air_baku: Boolean,
	val actuator_pompa_utama_1: Boolean,
	val actuator_pompa_utama_2: Boolean
)