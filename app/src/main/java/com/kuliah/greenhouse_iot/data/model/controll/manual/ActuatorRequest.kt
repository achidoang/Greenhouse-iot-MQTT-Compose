package com.kuliah.greenhouse_iot.data.model.controll.manual

data class ActuatorRequest(
	val topic: String,
	val payload: ActuatorPayload
)