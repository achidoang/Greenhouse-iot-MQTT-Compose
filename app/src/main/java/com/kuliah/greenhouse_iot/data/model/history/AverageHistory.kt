package com.kuliah.greenhouse_iot.data.model.history

data class AverageHistory(
	val year: Int,
	val week: Int? = null,
	val month: Int? = null,
	val day: Int? = null,
	val avg_watertemp: Float = 0f,
	val avg_waterppm: Float = 0f,
	val avg_waterph: Float = 0f,
	val avg_airtemp: Float = 0f,
	val avg_airhum: Float = 0f,
	val timeType: TimeType? = null // Pastikan timeType tidak null jika di-set manual
)
enum class TimeType {
	DAILY,
	WEEKLY,
	MONTHLY
}
