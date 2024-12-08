package com.kuliah.greenhouse_iot.presentation.screen.history


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.history.MonitoringHistory
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.viewmodel.history.HistoryViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
	val uiState by viewModel.uiState.collectAsState()
	val headColor = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground
	val bgColor = MaterialTheme.colorScheme.background
	val secBgColor = MaterialTheme.colorScheme.tertiaryContainer

	LaunchedEffect(Unit) {
		viewModel.loadHistory()
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("History") },
				modifier = Modifier.height(50.dp), // Reduced height
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = headColor
			)
		},
		containerColor = bgColor
	) { padding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding)
				.padding(horizontal = 16.dp, vertical = 8.dp)
				.padding(bottom = 85.dp)
		) {
			Card(
				modifier = Modifier.fillMaxWidth(),
				colors = CardDefaults.cardColors(containerColor = secBgColor),
				elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
			) {
				Column(modifier = Modifier.padding(4.dp)) {
					FilterSection(
						startDate = uiState.startDate,
						endDate = uiState.endDate,
						onStartDateChange = { viewModel.updateStartDate(it) },
						onEndDateChange = { viewModel.updateEndDate(it) },
						onFilterApply = { viewModel.loadHistory() },
						headColor = headColor,
						bgColor = secBgColor
					)

					Spacer(modifier = Modifier.height(4.dp))

					SortingSection(
						currentSortBy = uiState.sortBy,
						currentOrder = uiState.order,
						onSortChange = { sortBy, order ->
							viewModel.updateSorting(sortBy, order)
							viewModel.loadHistory()
						},
						headColor = headColor,
						bgColor = secBgColor
					)
				}
			}

			Spacer(modifier = Modifier.height(4.dp))

			if (uiState.isLoading) {
				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					LottieLoading()
				}
			} else {
				HistoryTable(uiState.history, headColor, textColor, secBgColor)

				Spacer(modifier = Modifier.height(16.dp))

				PaginationSection(
					currentPage = uiState.currentPage,
					totalPages = uiState.totalPages,
					onPageChanged = { newPage ->
						viewModel.updatePage(newPage)
						viewModel.loadHistory()
					},
					headColor = headColor,
					bgColor = secBgColor
				)
			}
		}
	}
}


@Composable
fun FilterSection(
	startDate: LocalDate?,
	endDate: LocalDate?,
	onStartDateChange: (LocalDate?) -> Unit,
	onEndDateChange: (LocalDate?) -> Unit,
	onFilterApply: () -> Unit,
	headColor: Color,
	bgColor: Color
) {
	var showStartDatePicker by remember { mutableStateOf(false) }
	var showEndDatePicker by remember { mutableStateOf(false) }

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Button(
			onClick = { showStartDatePicker = true },
			colors = ButtonDefaults.buttonColors(containerColor = bgColor)
		) {
			Text(startDate?.toString() ?: "Start Date", color = headColor)
		}
		Button(
			onClick = { showEndDatePicker = true },
			colors = ButtonDefaults.buttonColors(containerColor = bgColor)
		) {
			Text(endDate?.toString() ?: "End Date", color = headColor)
		}
		Button(
			onClick = onFilterApply,
			colors = ButtonDefaults.buttonColors(containerColor = bgColor)
		) {
			Text("Apply", color = headColor)
		}
	}

	if (showStartDatePicker) {
		DatePickerDialog(
			onDismissRequest = { showStartDatePicker = false },
			onDateSelected = {
				onStartDateChange(it)
				showStartDatePicker = false
			},
			title = "Select Start Date"
		)
	}

	if (showEndDatePicker) {
		DatePickerDialog(
			onDismissRequest = { showEndDatePicker = false },
			onDateSelected = {
				onEndDateChange(it)
				showEndDatePicker = false
			},
			title = "Select End Date"
		)
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
		Text("Sort by:", color = headColor)
		Box {
			Button(
				onClick = { expanded = true },
				colors = ButtonDefaults.buttonColors(containerColor = bgColor)
			) {
				Text(
					"$currentSortBy (${if (currentOrder == "ASC") "↑" else "↓"})",
					color = headColor
				)
			}
			DropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false }
			) {
				sortOptions.forEach { option ->
					DropdownMenuItem(
						text = { Text(option) },
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
fun HistoryTable(
	history: List<MonitoringHistory>,
	headColor: Color,
	textColor: Color,
	bgColor: Color
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = bgColor),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		val scrollState = rememberScrollState()
		Column {
			Row(
				modifier = Modifier
					.horizontalScroll(scrollState)
					.background(headColor.copy(alpha = 0.1f))
					.padding(vertical = 12.dp)
			) {
				HistoryTableHeader(headColor)
			}
			LazyColumn {
				items(history) { item ->
					Row(
						modifier = Modifier
							.horizontalScroll(scrollState)
							.padding(vertical = 12.dp)
					) {
						HistoryTableRow(item, textColor)
					}
					Divider(color = textColor.copy(alpha = 0.1f))
				}
			}
		}
	}
}

@Composable
fun HistoryTableHeader(headColor: Color) {
	TableCell(text = "Timestamp", width = 180.dp, color = headColor, bold = true)
	TableCell(text = "Water Temp", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Water PPM", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Water pH", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Air Temp", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Air Humidity", width = 120.dp, color = headColor, bold = true)
}

@Composable
fun HistoryTableRow(item: MonitoringHistory, textColor: Color) {
	val formattedTimestamp = item.timestamp.formatToReadableDate()
	TableCell(text = formattedTimestamp, width = 180.dp, color = textColor)
	TableCell(text = "%.1f°C".format(item.watertemp), width = 120.dp, color = textColor)
	TableCell(text = "%.1f".format(item.waterppm), width = 120.dp, color = textColor)
	TableCell(text = "%.1f".format(item.waterph), width = 120.dp, color = textColor)
	TableCell(text = "%.1f°C".format(item.airtemp), width = 120.dp, color = textColor)
	TableCell(text = "%.1f%%".format(item.airhum), width = 120.dp, color = textColor)
}

@Composable
fun TableCell(text: String, width: Dp, color: Color, bold: Boolean = false) {
	Text(
		text = text,
		modifier = Modifier
			.width(width)
			.padding(horizontal = 8.dp),
		style = MaterialTheme.typography.bodyMedium.copy(
			fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
		),
		color = color
	)
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
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
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
			style = MaterialTheme.typography.bodyLarge,
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
	title: String
) {
	val datePickerState = rememberDatePickerState()

	AlertDialog(
		onDismissRequest = onDismissRequest,
		title = { Text(title) },
		text = { DatePicker(state = datePickerState) },
		confirmButton = {
			TextButton(onClick = {
				datePickerState.selectedDateMillis?.let {
					onDateSelected(
						Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
					)
				}
				onDismissRequest()
			}) {
				Text("OK")
			}
		},
		dismissButton = {
			TextButton(onClick = onDismissRequest) {
				Text("Cancel")
			}
		}
	)
}

@SuppressLint("NewApi")
fun String.formatToReadableDate(): String {
	return try {
		val isoFormat = DateTimeFormatter.ISO_DATE_TIME
		val date = LocalDateTime.parse(this, isoFormat)
		date.format(DateTimeFormatter.ofPattern("dd, MMM yy, HH:mm"))
	} catch (e: Exception) {
		this // Return original string if parsing fails
	}
}