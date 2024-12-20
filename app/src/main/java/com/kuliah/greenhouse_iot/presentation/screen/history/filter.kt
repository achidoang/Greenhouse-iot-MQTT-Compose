package com.kuliah.greenhouse_iot.presentation.screen.history

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterAlt
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
	var alertMessage by remember { mutableStateOf("") }

	if (showAlert) {
		StyledAlertDialog(
			message = alertMessage,
			onDismiss = { showAlert = false }
		)
	}

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f))
			.padding(4.dp),
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			// Title
			Text(
				"Filter Data",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			// Reset Button
			if (startDate != null || endDate != null) {
				AnimatedVisibility(
					visible = true,
					enter = fadeIn() + expandVertically(),
					exit = fadeOut() + shrinkVertically()
				) {
					TextButton(
						onClick = onFilterReset,
//						modifier = Modifier.align(Alignment.End)
					) {
						Icon(
							Icons.Default.FilterAlt,
							contentDescription = null,
							modifier = Modifier.size(14.dp)
						)
						Spacer(modifier = Modifier.width(4.dp))
						Text("Reset Filters")
					}
				}
			}
		}

		// Date Range Section
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			EnhancedFilterButton(
				label = startDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
					?: "Start Date",
				onClick = { showStartDatePicker = true },
				icon = Icons.Default.CalendarToday,
				modifier = Modifier.weight(1f),
				isSelected = startDate != null
			)
			EnhancedFilterButton(
				label = endDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "End Date",
				onClick = { showEndDatePicker = true },
				icon = Icons.Default.CalendarMonth,
				modifier = Modifier.weight(1f),
				isSelected = endDate != null
			)
		}


	}

	if (showStartDatePicker) {
		EnhancedDatePickerDialog(
			onDismissRequest = { showStartDatePicker = false },
			onDateSelected = { selectedDate ->
				when {
					selectedDate > today -> {
						alertMessage = "Start date cannot be in the future"
						showAlert = true
					}

					endDate != null && selectedDate > endDate -> {
						alertMessage = "Start date cannot be after end date"
						showAlert = true
					}

					else -> onStartDateChange(selectedDate)
				}
				showStartDatePicker = false
			},
			title = "Select Start Date"
		)
	}

	if (showEndDatePicker) {
		EnhancedDatePickerDialog(
			onDismissRequest = { showEndDatePicker = false },
			onDateSelected = { selectedDate ->
				when {
					selectedDate > today -> {
						alertMessage = "End date cannot be in the future"
						showAlert = true
					}

					startDate != null && selectedDate < startDate -> {
						alertMessage = "End date cannot be before start date"
						showAlert = true
					}

					else -> onEndDateChange(selectedDate)
				}
				showEndDatePicker = false
			},
			title = "Select End Date"
		)
	}
}

@Composable
fun StyledAlertDialog(
	message: String,
	onDismiss: () -> Unit
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		confirmButton = {
			TextButton(
				onClick = onDismiss,
				modifier = Modifier
					.padding(horizontal = 16.dp, vertical = 8.dp)
			) {
				Text(
					text = "OK",
					style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
				)
			}
		},
		title = {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Icon(
					imageVector = Icons.Default.Error,
					contentDescription = "Alert Icon",
					tint = MaterialTheme.colorScheme.error
				)
				Text(
					text = "Invalid Date Selection",
					style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface)
				)
			}
		},
		text = {
			Text(
				text = message,
				style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
				modifier = Modifier.padding(vertical = 8.dp)
			)
		},
		containerColor = MaterialTheme.colorScheme.surface,
		tonalElevation = 6.dp,
		shape = MaterialTheme.shapes.medium
	)
}


@Composable
fun EnhancedFilterButton(
	label: String,
	onClick: () -> Unit,
	icon: ImageVector,
	modifier: Modifier = Modifier,
	isSelected: Boolean = false
) {
	Surface(
		onClick = onClick,
		modifier = modifier.height(48.dp),
		shape = RoundedCornerShape(12.dp),
		color = if (isSelected)
			MaterialTheme.colorScheme.secondaryContainer
		else
			MaterialTheme.colorScheme.surface,
		tonalElevation = if (isSelected) 0.dp else 2.dp
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 12.dp),
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				imageVector = icon,
				contentDescription = null,
				modifier = Modifier.size(20.dp),
				tint = if (isSelected)
					MaterialTheme.colorScheme.onSecondaryContainer
				else
					MaterialTheme.colorScheme.onSurfaceVariant
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = label,
				style = MaterialTheme.typography.bodyMedium,
				color = if (isSelected)
					MaterialTheme.colorScheme.onSecondaryContainer
				else
					MaterialTheme.colorScheme.onSurfaceVariant,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
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
		modifier = Modifier.fillMaxWidth().padding(4.dp),
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
		//			.padding(bottom = 20.dp),
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedDatePickerDialog(
	onDismissRequest: () -> Unit,
	onDateSelected: (LocalDate) -> Unit,
	title: String
) {
	val datePickerState = rememberDatePickerState()

	Dialog(onDismissRequest = onDismissRequest) {
		Surface(
			shape = MaterialTheme.shapes.extraLarge,
			tonalElevation = 6.dp,
			modifier = Modifier.padding(4.dp)
		) {
			Column(
				modifier = Modifier.padding(16.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onSurface
				)

				DatePicker(
					state = datePickerState,
					showModeToggle = false,
					modifier = Modifier.weight(1f, false)
				)

				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.CenterVertically
				) {
					TextButton(onClick = onDismissRequest) {
						Text("Cancel")
					}
					Spacer(modifier = Modifier.width(8.dp))
					Button(
						onClick = {
							datePickerState.selectedDateMillis?.let { selectedDateMillis ->
								val selectedDate = Instant.ofEpochMilli(selectedDateMillis)
									.atZone(ZoneId.systemDefault())
									.toLocalDate()
								onDateSelected(selectedDate)
							}
							onDismissRequest()
						}
					) {
						Text("Select")
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