package com.kuliah.greenhouse_iot.data.model

import com.google.gson.annotations.SerializedName
//
//data class MonitoringData(
//	@SerializedName("watertemp") val watertemp: Double,
//	@SerializedName("waterppm") val waterppm: Double,
//	@SerializedName("waterph") val waterph: Double,
//	@SerializedName("airtemp") val airtemp: Double,
//	@SerializedName("airhum") val airhum: Double
//)

data class MonitoringData(
	val watertemp: Float,
	val waterppm: Float,
	val waterph: Float,
	val airtemp: Float,
	val airhum: Float,
	val timestamp: String
)