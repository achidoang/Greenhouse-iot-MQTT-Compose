package com.kuliah.greenhouse_iot.presentation.screen.home

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kuliah.greenhouse_iot.data.model.weather.City
import com.kuliah.greenhouse_iot.data.model.weather.WeatherItem
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import com.kuliah.greenhouse_iot.presentation.viewmodel.weather.WeatherViewModel
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun CompactWeatherSection(
	weatherState: WeatherResponse?,
	weatherViewModel: WeatherViewModel
) {
	val bgColor = MaterialTheme.colorScheme.background
	weatherState?.data?.let { weatherData ->
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(bgColor)
		) {
			CompactWeatherCard(
				city = weatherData.city,
				weatherItem = weatherData.list[0]
			)
			Spacer(modifier = Modifier.height(8.dp))
			CompactHourlyForecastSection(forecasts = weatherData.list.take(24))
		}
	} ?: run {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(Color(0xFF1C1C1E)),
			contentAlignment = Alignment.Center
		) {
			Text(
				"Data cuaca belum tersedia.",
				color = Color.White,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

@SuppressLint("NewApi")
@Composable
fun CompactWeatherCard(
	city: City,
	weatherItem: WeatherItem
) {
	val lastUpdate = remember(weatherItem.dt) {
		val utcTime = Instant.ofEpochSecond(weatherItem.dt)
		val adjustedTime = utcTime.minus(Duration.ofHours(7))
		adjustedTime.atZone(ZoneId.of("Asia/Jakarta"))
			.format(DateTimeFormatter.ofPattern("dd MMM, EE"))
	}

	val bgColor = MaterialTheme.colorScheme.tertiaryContainer
	val bgCard = MaterialTheme.colorScheme.background
	val headText = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(20.dp))
			.shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
		colors = CardDefaults.cardColors(
			containerColor = bgColor
		)
	) {
		Column(
			modifier = Modifier
				.padding(12.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Column {
					Text(
						text = city.name,
						style = MaterialTheme.typography.titleMedium,
						color = headText,
						fontWeight = FontWeight.Bold
					)
					Text(
						text = lastUpdate,
						style = MaterialTheme.typography.bodyMedium,
						color = textColor
					)
				}
				WeatherIcon(
					description = weatherItem.weather[0].description,
					iconSize = 52.dp
				)
				Column {
					Text(
						text = "${weatherItem.main.temp.toInt()}°",
						style = MaterialTheme.typography.headlineSmall,
						color = headText,
						fontWeight = FontWeight.Bold
					)
					Text(
						text = weatherItem.weather[0].description.capitalize(),
						style = MaterialTheme.typography.bodySmall,
						color = textColor
					)
				}
			}


			Spacer(modifier = Modifier.height(8.dp))

			Card(
				modifier = Modifier.fillMaxWidth(),
				colors = CardDefaults.cardColors(
					containerColor = bgCard
				)
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(8.dp),
					horizontalArrangement = Arrangement.SpaceEvenly
				) {
					CompactWeatherMetric(
						icon = Icons.Default.Air,
						value = "${weatherItem.wind.speed}",
						unit = "m/s",
						label = "Wind"
					)
					VerticalDivider()
					CompactWeatherMetric(
						icon = Icons.Default.WaterDrop,
						value = "${weatherItem.main.humidity}",
						unit = "%",
						label = "Humidity"
					)
					VerticalDivider()
					CompactWeatherMetric(
						icon = Icons.Default.Umbrella,
						value = "100",
						unit = "%",
						label = "Rain"
					)
				}
			}
		}
	}
}

@Composable
fun CompactWeatherMetric(
	icon: ImageVector,
	value: String,
	unit: String,
	label: String
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Icon(
			imageVector = icon,
			contentDescription = label,
			tint = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier.size(14.dp)
		)
		Spacer(modifier = Modifier.height(2.dp))
		Row(
			verticalAlignment = Alignment.Bottom,
			horizontalArrangement = Arrangement.Center
		) {
			Text(
				text = value,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurface,
				fontWeight = FontWeight.Bold
			)
			Text(
				text = unit,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(start = 1.dp, bottom = 1.dp)
			)
		}
		Text(
			text = label,
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	}
}


@Composable
fun VerticalDivider() {
	Box(
		modifier = Modifier
			.height(28.dp)
			.width(2.dp)
			.background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
	)
}

