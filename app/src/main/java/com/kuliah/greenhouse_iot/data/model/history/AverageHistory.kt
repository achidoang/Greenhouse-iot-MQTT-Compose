package com.kuliah.greenhouse_iot.data.model.history

data class AverageHistory(
	val year: Int,
	val week: Int?, // Untuk weekly
	val month: Int?, // Untuk monthly/daily
	val day: Int?, // Untuk daily
	val avg_watertemp: Float,
	val avg_waterppm: Float,
	val avg_waterph: Float,
	val avg_airtemp: Float,
	val avg_airhum: Float,
	val timeType: TimeType // Tambahkan tipe waktu
)

enum class TimeType {
	DAILY,
	WEEKLY,
	MONTHLY
}
