package com.kuliah.greenhouse_iot.data.model.guide

import kotlinx.serialization.Serializable

@Serializable
data class Guide(
	val id: Int,
	val title: String,
	val description: String,
	val tools: List<String>,
	val steps: List<GuideStep>
)