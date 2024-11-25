package com.kuliah.greenhouse_iot.presentation.screen.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.history.MonitoringHistory
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.screen.add_user.DropdownMenuComponent
import com.kuliah.greenhouse_iot.presentation.viewmodel.history.HistoryViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
	var page by remember { mutableStateOf(1) }
	var limit by remember { mutableStateOf(10) }
	var startDate by remember { mutableStateOf<String?>(null) }
	var endDate by remember { mutableStateOf<String?>(null) }
	var sortBy by remember { mutableStateOf("timestamp") }
	var order by remember { mutableStateOf("DESC") }

	val history by viewModel.history.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()

	LaunchedEffect(page, limit, startDate, endDate, sortBy, order) {
		viewModel.loadHistoryPaginated(page, limit, startDate, endDate, sortBy, order)
	}

	Scaffold(
		topBar = { TopAppBar(title = { Text("History Monitoring") }) },
		content = { padding ->
			Column(
				modifier = Modifier
					.padding(padding)
					.fillMaxSize()
					.padding(16.dp)
			) {
				// Filter Section
				Text("Start Date")
				DatePickerField(value = startDate, onDateSelected = { startDate = it })

				Text("End Date")
				DatePickerField(value = endDate, onDateSelected = { endDate = it })

				Text("Limit Data")
				TextField(
					value = limit.toString(),
					onValueChange = { newValue ->
						limit = newValue.toIntOrNull() ?: 10 // Default limit 10 jika invalid
					},
					keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
					modifier = Modifier.fillMaxWidth()
				)

				Text("Sort By")
				DropdownMenuField(
					options = listOf("timestamp", "waterTemp", "waterPH"),
					selectedOption = sortBy,
					onOptionSelected = { sortBy = it }
				)

				Spacer(modifier = Modifier.height(16.dp))

				if (isLoading) {
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(padding),
						contentAlignment = Alignment.Center
					) {
						LottieLoading()
					}
				} else {
					LazyColumn(modifier = Modifier.weight(1f)) {
						items(history) { item -> HistoryItemRow(item = item) }
					}
				}

				Spacer(modifier = Modifier.height(16.dp))

				// Pagination Section
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Button(
						onClick = { if (page > 1) page -= 1 },
						enabled = page > 1
					) {
						Text("Previous")
					}
					Text("Page $page")
					Button(onClick = { page += 1 }) {
						Text("Next")
					}
				}
			}
		}
	)
}

@Composable
fun DatePickerField(value: String?, onDateSelected: (String) -> Unit) {
	var showPicker by remember { mutableStateOf(false) }

	if (showPicker) {
		DatePickerDialog(onDateSelected = {
			onDateSelected(it)
			showPicker = false
		}, onDismiss = { showPicker = false })
	}

	OutlinedTextField(
		value = value ?: "",
		onValueChange = {},
		label = { Text("Select Date") },
		readOnly = true,
		modifier = Modifier
			.fillMaxWidth()
			.clickable { showPicker = true }
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(
	options: List<String>,
	selectedOption: String,
	onOptionSelected: (String) -> Unit
) {
	var expanded by remember { mutableStateOf(false) }
	var currentOption by remember { mutableStateOf(selectedOption) }


	ExposedDropdownMenuBox(
		expanded = expanded,
		onExpandedChange = { expanded = !expanded }
	) {
		OutlinedTextField(
			value = currentOption,
			onValueChange = {},
			readOnly = true,
			label = { Text("Select Option") },
			trailingIcon = {
				ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
			},
			modifier = Modifier
				.menuAnchor()
				.fillMaxWidth()
		)
		ExposedDropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false }
		) {
			options.forEach { option ->
				androidx.compose.material3.DropdownMenuItem(
					text = { Text(option) },
					onClick = {
						onOptionSelected(option)
						currentOption = option
						expanded = false
					}
				)
			}
		}
	}
}

@Composable
fun FilterSection(
	startDate: String?,
	endDate: String?,
	sortBy: String,
	order: String,
	onDateSelected: (String, String) -> Unit,
	onSortChange: (String) -> Unit,
	onOrderChange: (String) -> Unit
) {
	Column {
		// Date Filters
		Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
			DateFilterField(label = "Start Date", dateValue = startDate) { date -> onDateSelected("Start Date", date) }
			DateFilterField(label = "End Date", dateValue = endDate) { date -> onDateSelected("End Date", date) }
		}

		Spacer(modifier = Modifier.height(8.dp))

		// Sorting & Order
		Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
			DropdownMenuComponent(
				selectedValue = sortBy,
				label = "Sort By",
				options = listOf("timestamp", "watertemp", "waterppm", "airtemp"),
				onOptionSelected = onSortChange
			)
			DropdownMenuComponent(
				selectedValue = order,
				label = "Order",
				options = listOf("ASC", "DESC"),
				onOptionSelected = onOrderChange
			)
		}
	}
}

@Composable
fun PaginationControls(page: Int, onPrevious: () -> Unit, onNext: () -> Unit) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Button(onClick = onPrevious, enabled = page > 1) { Text("Previous") }
		Text("Page $page", style = MaterialTheme.typography.bodyLarge)
		Button(onClick = onNext) { Text("Next") }
	}
}

@Composable
fun HistoryItemRow(item: MonitoringHistory) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Column(modifier = Modifier.padding(8.dp)) {
			Text("Timestamp: ${item.timestamp}", style = MaterialTheme.typography.bodyMedium)
			Text("Water Temp: ${item.watertemp} °C", style = MaterialTheme.typography.bodyMedium)
			Text("Water PPM: ${item.waterppm}", style = MaterialTheme.typography.bodyMedium)
			Text("Water PH: ${item.waterph}", style = MaterialTheme.typography.bodyMedium)
			Text("Air Temp: ${item.airtemp} °C", style = MaterialTheme.typography.bodyMedium)
			Text("Air Humidity: ${item.airhum} %", style = MaterialTheme.typography.bodyMedium)
		}
	}
}

@Composable
fun DateFilterField(
	label: String,
	dateValue: String?,
	onDateSelected: (String) -> Unit
) {
	var isDatePickerVisible by remember { mutableStateOf(false) }

	if (isDatePickerVisible) {
		DatePickerDialog(
			onDateSelected = { selectedDate ->
				onDateSelected(selectedDate)
				isDatePickerVisible = false
			},
			onDismiss = { isDatePickerVisible = false }
		)
	}

	TextField(
		value = dateValue ?: "",
		onValueChange = {},
		label = { Text(label) },
		modifier = Modifier
			.clickable { isDatePickerVisible = true }
			.fillMaxWidth(),
		readOnly = true
	)
}

@Composable
fun DatePickerDialog(
	onDateSelected: (String) -> Unit,
	onDismiss: () -> Unit
) {
	val context = LocalContext.current
	val calendar = Calendar.getInstance()

	val datePickerDialog = android.app.DatePickerDialog(
		context,
		{ _, year, month, dayOfMonth ->
			val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
			onDateSelected(selectedDate)
		},
		calendar.get(Calendar.YEAR),
		calendar.get(Calendar.MONTH),
		calendar.get(Calendar.DAY_OF_MONTH)
	)

	DisposableEffect(Unit) {
		datePickerDialog.setOnDismissListener { onDismiss() }
		datePickerDialog.show()
		onDispose { datePickerDialog.dismiss() }
	}
}
