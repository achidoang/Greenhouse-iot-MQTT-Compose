package com.kuliah.greenhouse_iot.data.model.controll.manual

data class ActuatorPayload(
	val actuator_nutrisi: Int,
	val actuator_ph_up: Int,
	val actuator_ph_down: Int,
	val actuator_air_baku: Int,
	val actuator_pompa_utama_1: Int,
	val actuator_pompa_utama_2: Int
)