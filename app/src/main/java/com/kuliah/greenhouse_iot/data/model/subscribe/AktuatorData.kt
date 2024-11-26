package com.kuliah.greenhouse_iot.data.model.subscribe

import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorPayload

data class AktuatorData(
	val timestamp: String,
	val actuator_nutrisi: Boolean,
	val actuator_ph_up: Boolean,
	val actuator_ph_down: Boolean,
	val actuator_air_baku: Boolean,
	val actuator_pompa_utama_1: Boolean,
	val actuator_pompa_utama_2: Boolean
)

fun AktuatorData.toActuatorPayload(): ActuatorPayload {
	return ActuatorPayload(
		actuator_nutrisi = if (actuator_nutrisi) 1 else 0,
		actuator_ph_up = if (actuator_ph_up) 1 else 0,
		actuator_ph_down = if (actuator_ph_down) 1 else 0,
		actuator_air_baku = if (actuator_air_baku) 1 else 0,
		actuator_pompa_utama_1 = if (actuator_pompa_utama_1) 1 else 0,
		actuator_pompa_utama_2 = if (actuator_pompa_utama_2) 1 else 0
	)
}


