package com.kuliah.greenhouse_iot.data.model.guide

import kotlinx.serialization.Serializable


@Serializable
data class GuideStep(
	val title: String,
	val description: List<String>
)