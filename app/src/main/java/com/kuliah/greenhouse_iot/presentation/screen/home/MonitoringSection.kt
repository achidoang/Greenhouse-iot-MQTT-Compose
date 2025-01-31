package com.kuliah.greenhouse_iot.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kuliah.greenhouse_iot.data.model.subscribe.MonitoringData


@SuppressLint("NewApi")
@Composable
fun MonitoringSection(
	monitoringData: MonitoringData,
	deviceStatus: String
) {
	val bgColor = MaterialTheme.colorScheme.background
	val headColor = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(bgColor)
			.padding(vertical = 8.dp) // Reduced padding
	) {
		// Header with Water Nutrition and Device Status
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					text = "Nutrisi Air",
					style = MaterialTheme.typography.titleMedium,
					color = headColor
				)
				Spacer(modifier = Modifier.width(4.dp))
				Icon(
					imageVector = Icons.Default.WaterDrop,
					contentDescription = null,
					tint = Color(0xFF4CAF50),
					modifier = Modifier.size(16.dp)
				)
				Spacer(modifier = Modifier.width(4.dp))
				Text(
					text = "${monitoringData.waterppm} ppm",
					color = headColor,
					style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
				)
			}

			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(4.dp)
			) {
				Box(
					modifier = Modifier
						.size(6.dp)
						.background(
							when (deviceStatus) {
								"Online" -> Color(0xFF4CAF50)
								"Offline" -> Color(0xFFF44336)
								else -> Color(0xFFFFC107)
							},
							shape = CircleShape
						)
				)
				Text(
					text = deviceStatus,
					style = MaterialTheme.typography.bodySmall,
					color = textColor
				)
			}
		}

		Spacer(modifier = Modifier.height(8.dp))

		// Monitoring Cards Grid
		LazyVerticalGrid(
			columns = GridCells.Fixed(2),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp),
		) {
			item {
				CompactMonitoringCard(
					value = monitoringData.waterph,
					maxValue = 14f,
					label = "pH Air",
					showProgress = true,
					icon = Icons.Default.Science,
					color = Color(0xFF2196F3)
				)
			}
			item {
				CompactMonitoringCard(
					value = monitoringData.airhum,
					maxValue = 100f,
					label = "Kelembaban",
					unit = "%",
					showProgress = true,
					icon = Icons.Default.WaterDrop,
					color = Color(0xFF9C27B0)
				)
			}
			item {
				CompactMonitoringCard(
					value = monitoringData.watertemp,
					maxValue = 100f,
					label = "Suhu Air",
					unit = "°C",
					showProgress = false,
					icon = Icons.Default.Thermostat,
					color = Color(0xFFFF9800)
				)
			}
			item {
				CompactMonitoringCard(
					value = monitoringData.airtemp,
					maxValue = 100f,
					label = "Suhu Udara",
					unit = "°C",
					showProgress = false,
					icon = Icons.Default.DeviceThermostat,
					color = Color(0xFFE91E63)
				)
			}
		}

		// Timestamp as small text
		Text(
			text = "Update: ${formatTimestamp(monitoringData.timestamp)}",
			style = MaterialTheme.typography.bodySmall,
			color = Color.Gray,
			modifier = Modifier.padding(top = 4.dp)
		)
	}
}

@Composable
fun CompactMonitoringCard(
	value: Float,
	maxValue: Float,
	label: String,
	unit: String = "",
	showProgress: Boolean = false,
	icon: ImageVector,
	color: Color
) {
	val bgColor = MaterialTheme.colorScheme.tertiaryContainer
	val headColor = MaterialTheme.colorScheme.onSurface
	val trackColor = MaterialTheme.colorScheme.surface
	val textColor = MaterialTheme.colorScheme.onBackground

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1.8f), // More compact ratio
		colors = CardDefaults.cardColors(
			containerColor = bgColor
		),
		shape = RoundedCornerShape(12.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(8.dp),
			verticalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = label,
					style = MaterialTheme.typography.bodyMedium,
					color = textColor
				)
				Icon(
					imageVector = icon,
					contentDescription = null,
					tint = color,
					modifier = Modifier.size(20.dp)
				)
			}

			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(4.dp)
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center,
					modifier = Modifier.fillMaxWidth()
				) {
					Text(
						text = String.format("%.1f", value),
						color = headColor,
						style = MaterialTheme.typography.headlineLarge.copy(
							fontWeight = FontWeight.Bold
						)
					)
					if (unit.isNotEmpty()) {
						Text(
							text = unit,
							style = MaterialTheme.typography.titleLarge,
							color = textColor,
							modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
						)
					}
				}

			}
			if (showProgress) {
				LinearProgressIndicator(
					progress = value / maxValue,
					modifier = Modifier
						.fillMaxWidth()
						.height(4.dp)
						.clip(RoundedCornerShape(2.dp)),
					color = color,
					trackColor = trackColor
				)
			} else {
				LinearProgressIndicator(
					progress = value / maxValue,
					modifier = Modifier
						.fillMaxWidth()
						.height(4.dp)
						.clip(RoundedCornerShape(2.dp)),
					color = MaterialTheme.colorScheme.tertiaryContainer,
					trackColor = MaterialTheme.colorScheme.tertiaryContainer
				)
			}
		}
	}
}


@SuppressLint("NewApi")
fun formatTimestamp(timestamp: String): String {
	return try {
		val instant = java.time.Instant.parse(timestamp)
		val dateTime = java.time.LocalDateTime.ofInstant(
			instant,
			java.time.ZoneId.systemDefault()
		)
		java.time.format.DateTimeFormatter
			.ofPattern("dd MMM yyyy, HH:mm:ss")
			.format(dateTime)
	} catch (e: Exception) {
		"Invalid Date"
	}
}