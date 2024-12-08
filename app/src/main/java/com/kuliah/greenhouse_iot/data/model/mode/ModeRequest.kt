package com.kuliah.greenhouse_iot.data.model.mode

data class ModeRequest(
	val topic: String,
	val payload: Map<String, Int>
)
