package com.kuliah.greenhouse_iot.presentation.screen.history

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun FilterSection(
	startDate: LocalDate?,
	endDate: LocalDate?,
	onStartDateChange: (LocalDate?) -> Unit,
	onEndDateChange: (LocalDate?) -> Unit,
	onFilterReset: () -> Unit,
	headColor: Color,
	bgColor: Color
) {
	val today = LocalDate.now()
	var showStartDatePicker by remember { mutableStateOf(false) }
	var showEndDatePicker by remember { mutableStateOf(false) }
	var showAlert by remember { mutableStateOf(false) }

	if (showAlert) {
		AlertDialog(
			onDismissRequest = { showAlert = false },
			confirmButton = {
				TextButton(onClick = { showAlert = false }) {
					Text("OK")
				}
			},
			title = { Text("Invalid Date Range") },
			text = { Text("End date cannot be earlier than start date.") }
		)
	}

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		FilterButton(
			label = startDate?.toString() ?: "Start Date",
			onClick = { showStartDatePicker = true },
			headColor = headColor,
			bgColor = bgColor
		)
		FilterButton(
			label = endDate?.toString() ?: "End Date",
			onClick = { showEndDatePicker = true },
			headColor = headColor,
			bgColor = bgColor
		)
		Button(
			onClick = { onFilterReset() },
			colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
		) {
			Text("Reset Filter", color = MaterialTheme.colorScheme.onSurface)
		}
	}

	if (showStartDatePicker) {
		DatePickerDialog(
			onDismissRequest = { showStartDatePicker = false },
			onDateSelected = { selectedDate ->
				if (endDate != null && selectedDate > endDate) {
					showAlert = true
				} else {
					onStartDateChange(selectedDate)
				}
				showStartDatePicker = false
			},
			title = "Select Start Date",
			endDate = endDate
		)
	}

	if (showEndDatePicker) {
		DatePickerDialog(
			onDismissRequest = { showEndDatePicker = false },
			onDateSelected = { selectedDate ->
				if (startDate != null && selectedDate < startDate) {
					showAlert = true
				} else {
					onEndDateChange(selectedDate)
				}
				showEndDatePicker = false
			},
			title = "Select End Date",
			startDate = startDate
		)
	}
}

@Composable
fun FilterButton(label: String, onClick: () -> Unit, headColor: Color, bgColor: Color) {
	OutlinedButton(
		onClick = onClick,
		colors = ButtonDefaults.outlinedButtonColors(contentColor = headColor),
		border = BorderStroke(1.dp, headColor)
	) {
		Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
	}
}

@Composable
fun SortingSection(
	currentSortBy: String,
	currentOrder: String,
	onSortChange: (String, String) -> Unit,
	headColor: Color,
	bgColor: Color
) {
	var expanded by remember { mutableStateOf(false) }
	val sortOptions = listOf("timestamp", "watertemp", "waterppm", "waterph", "airtemp", "airhum")

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text("Sort by:", color = headColor, style = MaterialTheme.typography.bodyMedium)
		Box {
			OutlinedButton(
				onClick = { expanded = true },
				colors = ButtonDefaults.outlinedButtonColors(contentColor = headColor),
				border = BorderStroke(1.dp, headColor)
			) {
				Text("${currentSortBy.capitalize()} (${if (currentOrder == "ASC") "↑" else "↓"})")
			}
			DropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false }
			) {
				sortOptions.forEach { option ->
					DropdownMenuItem(
						text = { Text(option.capitalize()) },
						onClick = {
							onSortChange(
								option,
								if (option == currentSortBy && currentOrder == "ASC") "DESC" else "ASC"
							)
							expanded = false
						}
					)
				}
			}
		}
	}
}


@Composable
fun PaginationSection(
	currentPage: Int,
	totalPages: Int,
	onPageChanged: (Int) -> Unit,
	headColor: Color,
	bgColor: Color
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		IconButton(
			onClick = { if (currentPage > 1) onPageChanged(currentPage - 1) },
			enabled = currentPage > 1
		) {
			Icon(Icons.Default.ArrowBack, contentDescription = "Previous Page", tint = headColor)
		}
		Text(
			"Page $currentPage of $totalPages",
			style = MaterialTheme.typography.bodyMedium,
			color = headColor
		)
		IconButton(
			onClick = { if (currentPage < totalPages) onPageChanged(currentPage + 1) },
			enabled = currentPage < totalPages
		) {
			Icon(Icons.Default.ArrowForward, contentDescription = "Next Page", tint = headColor)
		}
	}
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
	onDismissRequest: () -> Unit,
	onDateSelected: (LocalDate) -> Unit,
	title: String,
	startDate: LocalDate? = null,
	endDate: LocalDate? = null,
	isStartDatePicker: Boolean = true
) {
	val datePickerState = rememberDatePickerState()

	Dialog(onDismissRequest = onDismissRequest) {
		Surface(
			shape = MaterialTheme.shapes.medium,
			tonalElevation = 6.dp,
			modifier = Modifier.padding(16.dp)
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleLarge,
					modifier = Modifier.padding(bottom = 16.dp)
				)
				DatePicker(
					state = datePickerState,
					showModeToggle = false
				)
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 16.dp),
					horizontalArrangement = Arrangement.End
				) {
					TextButton(onClick = onDismissRequest) {
						Text("Cancel")
					}
					TextButton(
						onClick = {
							datePickerState.selectedDateMillis?.let { selectedDateMillis ->
								val selectedDate = Instant.ofEpochMilli(selectedDateMillis)
									.atZone(ZoneId.systemDefault())
									.toLocalDate()

								// Validasi tanggal
								val today = LocalDate.now()
								if (selectedDate <= today) {
									if (isStartDatePicker) {
										if (endDate == null || selectedDate <= endDate) {
											onDateSelected(selectedDate)
										}
									} else {
										if (startDate == null || selectedDate >= startDate) {
											onDateSelected(selectedDate)
										}
									}
								}
							}
							onDismissRequest()
						}
					) {
						Text("OK")
					}
				}
			}
		}
	}
}

@SuppressLint("NewApi")
fun String.formatToReadableDate(): String {
	return try {
		val isoFormat = DateTimeFormatter.ISO_DATE_TIME
		val date = LocalDateTime.parse(this, isoFormat)
		date.format(DateTimeFormatter.ofPattern("dd MMM yy, HH:mm"))
	} catch (e: Exception) {
		this // Return original string if parsing fails
	}
}