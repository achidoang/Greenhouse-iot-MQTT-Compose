package com.kuliah.greenhouse_iot.presentation.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring.MonitoringViewModel

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

	Box(modifier = Modifier.fillMaxSize()) {
		// Logout button
		IconButton(
			onClick = {
				navController.navigate(Route.Manage.destination) {
					popUpTo(Route.Home.destination) { inclusive = false } // Bersihkan stack hingga Home
				}
			},
			modifier = Modifier
				.align(Alignment.TopEnd)
				.padding(16.dp)
		) {
			Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
		}

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(modifier = Modifier.height(30.dp))

			Text(
				text = "Data Monitoring",
				style = MaterialTheme.typography.titleLarge,
				modifier = Modifier.padding(bottom = 16.dp)
			)

			if (monitoringData != null) {
				// PPM data at the top
				Spacer(modifier = Modifier.height(10.dp))

				MonitoringCircle(
					value = monitoringData.waterppm,
					maxValue = 1000f,
					label = "PPM Air",
					unit = "ppm",
					size = 150.dp, // Size for main data
					fontSize = MaterialTheme.typography.titleLarge.fontSize // Larger font
				)

				Spacer(modifier = Modifier.height(24.dp))

				// pH Air and Suhu Air below PPM
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceAround
				) {
					MonitoringCircle(
						value = monitoringData.waterph,
						maxValue = 14f,
						label = "pH Air",
						size = 70.dp,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize
					)
					MonitoringCircle(
						value = monitoringData.watertemp,
						maxValue = 50f,
						label = "Suhu Air",
						unit = "°C",
						size = 70.dp,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize
					)
				}

				Spacer(modifier = Modifier.height(24.dp))

				// Suhu Udara and Kelembapan Udara below pH Air and Suhu Air
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceAround
				) {
					MonitoringCircle(
						value = monitoringData.airtemp,
						maxValue = 100f,
						label = "Suhu Udara",
						unit = "°C",
						size = 70.dp,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize
					)
					MonitoringCircle(
						value = monitoringData.airhum,
						maxValue = 100f,
						label = "Kelembapan",
						unit = "%",
						size = 70.dp,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize
					)
				}

				Spacer(modifier = Modifier.height(24.dp))

				// Timestamp
				val formattedTimestamp = monitoringData.timestamp.let {
					try {
						val instant = java.time.Instant.parse(it)
						val dateTime = java.time.LocalDateTime.ofInstant(
							instant,
							java.time.ZoneId.systemDefault()
						)
						java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")
							.format(dateTime)
					} catch (e: Exception) {
						"Invalid Date"
					}
				}

				Text(
					text = "Last Update: $formattedTimestamp",
					style = MaterialTheme.typography.bodySmall,
					color = Color.Gray
				)

				// kode ini benar, ketika tidak ada data dalam 10 detik maka device dinyatakan offline, namun masih salah, ketika hp saya tidak ada internet, dia tidak menampilkan no internet connection
				// Menampilkan status perangkat
				Text(
					text = "Device Status: $deviceStatus",
					style = MaterialTheme.typography.bodyMedium,
					color = when (deviceStatus) {
						"Online" -> Color.Green
						"Offline" -> Color.Red
						else -> Color.Yellow // Untuk No Internet
					}
				)
			} else {
				Text(text = "Mengambil data...", style = MaterialTheme.typography.bodyLarge)
			}
		}
	}
}


@Composable
fun MonitoringCircle(
	value: Float,
	maxValue: Float,
	label: String,
	unit: String = "",
	size: Dp = 100.dp,
	fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize
) {
	val progress = value / maxValue

	Box(
		modifier = Modifier.size(size),
		contentAlignment = Alignment.Center
	) {
		// Circular Progress Indicator
		Canvas(modifier = Modifier.fillMaxSize()) {
			drawArc(
				color = Color(0xFF03DAC5), // Warna progress
				startAngle = -90f,
				sweepAngle = 360 * progress,
				useCenter = false,
				style = Stroke(
					width = (size / 12).toPx(),
					cap = StrokeCap.Round
				) // Adjust stroke size
			)
		}

		// Circle Content
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text(
				text = value.toInt().toString(),
				style = MaterialTheme.typography.bodyLarge.copy(fontSize = fontSize)
			)
			if (unit.isNotEmpty()) {
				Text(
					text = unit,
					style = MaterialTheme.typography.bodySmall.copy(fontSize = fontSize / 1.5),
					color = Color.Gray
				)
			}
			Text(
				text = label,
				style = MaterialTheme.typography.bodySmall.copy(fontSize = fontSize / 1.5),
				color = Color.Gray
			)
		}
	}
}
