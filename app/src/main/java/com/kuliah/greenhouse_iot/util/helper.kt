package com.kuliah.greenhouse_iot.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun convertMinutesToHoursAndMinutes(minutes: Int): String {
	val hours = minutes / 60
	val remainingMinutes = minutes % 60
	return "$hours hr $remainingMinutes mins"
}

fun separateWithCommas(number: Long): String {
	val formatter: NumberFormat = DecimalFormat("#,###")
	return formatter.format(number)
}

fun convertToPercentage(figure: Double): String {
	val percentage = (figure * 10).toInt()
	return percentage.toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(releaseDate: String): String {
	val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
	val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
	val date = LocalDate.parse(releaseDate, inputFormatter)
	return outputFormatter.format(date)
}

