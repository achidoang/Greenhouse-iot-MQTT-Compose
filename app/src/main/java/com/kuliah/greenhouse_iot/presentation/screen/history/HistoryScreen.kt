package com.kuliah.greenhouse_iot.presentation.screen.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.DropdownMenuItem
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
	val history by viewModel.history.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()

	var page by remember { mutableStateOf(1) }
	var limit by remember { mutableStateOf(50) }

	LaunchedEffect(page, limit) {
		viewModel.loadHistoryPaginated(page, limit, null, null, "timestamp", "desc")
	}

	Scaffold(
		topBar = {
			TopAppBar(title = { Text("History Monitoring") })
		},
		content = { padding ->
			Column(modifier = Modifier.padding(padding).padding(4.dp).padding(bottom = 80.dp)) {
				// Table Section
				if (isLoading) {
					Box(
						modifier = Modifier
							.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						LottieLoading()
					}
				} else {
					LazyColumn(modifier = Modifier.weight(1f)) {
						item {
							HistoryTableHeader()
						}
						items(history) { item ->
							HistoryTableRow(item)
						}
					}
				}

				Spacer(modifier = Modifier.height(16.dp))

				// Pagination Section
				PaginationSection(
					page = page,
					onPageChanged = { page = it },
					hasNextPage = history.size == limit
				)
			}
		}
	)
}

@Composable
fun HistoryTableHeader() {
	Row(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.primary)
			.padding(8.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text("Timestamp", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
		Text("Water Temp (°C)", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
		Text("Water PPM", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
		Text("Air Temp (°C)", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
	}
}

@Composable
fun HistoryTableRow(item: MonitoringHistory) {
	// Format timestamp menjadi tanggal yang lebih mudah dibaca
	val formattedTimestamp = item.timestamp.formatToReadableDate()

	Row(
		modifier = Modifier
			.padding(8.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(formattedTimestamp, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
		Text("${item.watertemp}°C", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
		Text("${item.waterppm}", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
		Text("${item.airtemp}°C", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
	}
}


@Composable
fun PaginationSection(page: Int, onPageChanged: (Int) -> Unit, hasNextPage: Boolean) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Button(onClick = { if (page > 1) onPageChanged(page - 1) }, enabled = page > 1) {
			Text("Previous")
		}
		Text("Page $page", style = MaterialTheme.typography.bodyLarge)
		Button(onClick = { if (hasNextPage) onPageChanged(page + 1) }, enabled = hasNextPage) {
			Text("Next")
		}
	}
}


fun String.formatToReadableDate(): String {
	return try {
		val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
		isoFormat.timeZone = TimeZone.getTimeZone("UTC")
		val date = isoFormat.parse(this)

		val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
		outputFormat.format(date ?: Date())
	} catch (e: Exception) {
		this // Return original string jika parsing gagal
	}
}