package com.kuliah.greenhouse_iot.presentation.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring.MonitoringViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	monitoringViewModel: MonitoringViewModel = hiltViewModel(),
) {
	val monitoringData = monitoringViewModel.monitoringData.collectAsState().value
	val deviceStatus by monitoringViewModel.deviceStatus.collectAsState()

	// Start service when the screen is loaded
	LaunchedEffect(Unit) {
		monitoringViewModel.startMonitoringService()
	}

	DisposableEffect(Unit) {
		onDispose {
			monitoringViewModel.stopMonitoringService()
		}
	}

	val backgroundColor = MaterialTheme.colorScheme.background
	val textColor = MaterialTheme.colorScheme.onSurface
	val headColor = MaterialTheme.colorScheme.onBackground


	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(backgroundColor)
			.padding(16.dp)
	) {
		// Header with Settings button
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 8.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Monitoring Realtime",
				style = MaterialTheme.typography.titleLarge.copy(
					fontWeight = FontWeight.Bold,
				),
				color = headColor
			)
			IconButton(onClick = {
				navController.navigate(Route.Manage.destination) {
					popUpTo(Route.Home.destination) { inclusive = false }
				}
			}) {
				Icon(
					imageVector = Icons.Default.Person2,
					contentDescription = "Settings",
					tint = Color.Gray
				)
			}
		}

		if (monitoringData != null) {
			// Main progress section
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 16.dp)
			) {
				Text(
					text = "Nutrisi Air",
					style = MaterialTheme.typography.bodyMedium,
					color = textColor
				)

				Row(
					modifier = Modifier.padding(vertical = 8.dp),
					verticalAlignment = Alignment.Bottom
				) {
					Text(
						text = "${monitoringData.waterppm.toInt()}",
						color = textColor,
						style = MaterialTheme.typography.headlineMedium.copy(
							fontWeight = FontWeight.Bold
						)
					)
					Text(
						text = " ppm",
						style = MaterialTheme.typography.bodyLarge,
						color = textColor,
						modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
					)
				}

				// Custom progress bar
				LinearProgressIndicator(
					progress = monitoringData.waterppm / 1000f,
					modifier = Modifier
						.fillMaxWidth()
						.height(8.dp)
						.clip(RoundedCornerShape(4.dp)),
					color = Color(0xFF4CAF50),
					trackColor = Color(0xFFE0E0E0)
				)
			}

			// Grid of monitoring values
			LazyVerticalGrid(
				columns = GridCells.Fixed(2),
				horizontalArrangement = Arrangement.spacedBy(26.dp),
				verticalArrangement = Arrangement.spacedBy(26.dp),
				modifier = Modifier.padding(top = 16.dp)
			) {
				item {
					MonitoringCard(
						value = monitoringData.waterph,
						maxValue = 14f,
						label = "pH Air",
						color = Color(0xFF2196F3)
					)
				}
				item {
					MonitoringCard(
						value = monitoringData.watertemp,
						maxValue = 100f,
						label = "Suhu Air",
						unit = "°C",
						color = Color(0xFFFF9800)
					)
				}
				item {
					MonitoringCard(
						value = monitoringData.airtemp,
						maxValue = 100f,
						label = "Suhu Udara",
						unit = "°C",
						color = Color(0xFFE91E63)
					)
				}
				item {
					MonitoringCard(
						value = monitoringData.airhum,
						maxValue = 100f,
						label = "Kelembaban",
						unit = "%",
						color = Color(0xFF9C27B0)
					)
				}
			}

			// Status section
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 16.dp)
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Device Status:",
						style = MaterialTheme.typography.bodyMedium,
						color = Color.Gray
					)
					Text(
						text = deviceStatus,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.Bold
						),
						color = when (deviceStatus) {
							"Online" -> Color(0xFF4CAF50)
							"Offline" -> Color(0xFFF44336)
							else -> Color(0xFFFFC107)
						}
					)
				}

				// Timestamp
				val formattedTimestamp = monitoringData.timestamp.let {
					try {
						val instant = Instant.parse(it)
						val dateTime = LocalDateTime.ofInstant(
							instant,
							ZoneId.systemDefault()
						)
						DateTimeFormatter
							.ofPattern("dd MMMM yyyy, HH:mm:ss")
							.format(dateTime)
					} catch (e: Exception) {
						"Invalid Date"
					}
				}

				Text(
					text = "Last Update: $formattedTimestamp",
					style = MaterialTheme.typography.bodySmall,
					color = Color.Gray,
					modifier = Modifier.padding(top = 8.dp)
				)
			}
		} else {
			Box(
				modifier = Modifier
					.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				LottieLoading()
			}
		}
	}
}

@Composable
fun MonitoringCard(
	value: Float,
	maxValue: Float,
	label: String,
	unit: String = "",
	color: Color
) {

	val textColor = MaterialTheme.colorScheme.onSurface
	val backColor = MaterialTheme.colorScheme.background

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1.9f),
		elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
		colors = CardDefaults.cardColors(containerColor = backColor),
		border = BorderStroke(1.dp, textColor),
	) {
		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(6.dp),
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier
					.size(65.dp)
					.padding(8.dp)
			) {
				CircularProgressIndicator(
					progress = 1f,
					modifier = Modifier.fillMaxSize(),
					color = Color(0xFFE0E0E0),
					strokeWidth = 6.dp
				)
				CircularProgressIndicator(
					progress = value / maxValue,
					modifier = Modifier.fillMaxSize(),
					color = color,
					strokeWidth = 6.dp
				)
			}
			Column(
				modifier = Modifier.padding(8.dp),
				horizontalAlignment = Alignment.Start,
				verticalArrangement = Arrangement.Center,
			) {
				Text(
					text = label,
					style = MaterialTheme.typography.labelMedium,
					color = textColor,
				)
				Row(
					horizontalArrangement = Arrangement.Start,
					verticalAlignment = Alignment.CenterVertically,
				) {
					Text(
						text = value.toInt().toString(),
						color = textColor,
						style = MaterialTheme.typography.titleMedium.copy(
							fontWeight = FontWeight.Bold
						)
					)
					if (unit.isNotEmpty()) {
						Text(
							text = unit,
							style = MaterialTheme.typography.bodySmall,
							color = textColor
						)
					}
				}
			}

		}
	}
}
