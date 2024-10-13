package com.kuliah.greenhouse_iot.data.model

import com.google.gson.annotations.SerializedName

data class ActuatorStatus(
//	val actuator_nutrisi: Int = 0,
	@SerializedName("actuatorNutrisi") val actuator_nutrisi: Int = 0,
	val actuator_ph_up: Int = 0,
	val actuator_ph_down: Int = 0,
	val actuator_air_baku: Int = 0,
	val actuator_pompa_utama_1: Int = 0,
	val actuator_pompa_utama_2: Int = 0,
)


