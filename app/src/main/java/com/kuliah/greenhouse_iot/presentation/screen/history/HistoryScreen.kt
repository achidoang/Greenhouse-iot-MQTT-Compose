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
				modifier = Modifier.height(50.dp),
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = headColor
			)
		},
		containerColor = bgColor
	) { padding ->
		Spacer(modifier = Modifier.height(16.dp))

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding)
				.padding(horizontal = 8.dp, vertical = 4.dp)
				.padding(bottom = 90.dp)
		) {
			Card(
				modifier = Modifier.fillMaxWidth(),
				colors = CardDefaults.cardColors(containerColor = secBgColor),
				elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
			) {
				Column(modifier = Modifier.padding(8.dp)) {
					FilterSection(
						startDate = uiState.startDate,
						endDate = uiState.endDate,
						onStartDateChange = {
							viewModel.updateStartDate(it)
							viewModel.loadHistory()
						},
						onEndDateChange = {
							viewModel.updateEndDate(it)
							viewModel.loadHistory()
						},
						onFilterReset = {
							viewModel.updateStartDate(null)
							viewModel.updateEndDate(null)
							viewModel.loadHistory()
						},
						headColor = headColor,
						bgColor = secBgColor
					)
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

			if (uiState.isLoading) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}
			} else {
				Spacer(modifier = Modifier.height(12.dp))
				HistoryTable(uiState.history, headColor, textColor, secBgColor)
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
