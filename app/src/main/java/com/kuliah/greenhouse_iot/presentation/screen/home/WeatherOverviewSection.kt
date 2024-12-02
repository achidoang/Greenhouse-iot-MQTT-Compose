package com.kuliah.greenhouse_iot.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kuliah.greenhouse_iot.data.model.weather.City
import com.kuliah.greenhouse_iot.data.model.weather.WeatherItem

@Composable
fun WeatherOverviewSection(city: City, weatherItem: WeatherItem) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp)
			.clip(RoundedCornerShape(16.dp)),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					Brush.verticalGradient(
						colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC))
					)
				)
				.padding(12.dp)
		) {
			Column {
				// City information
				Text(
					text = "${city.name}, ${city.country}",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold,
					color = Color.White
				)
				Text(
					text = "Zona waktu: ${city.timezone / 3600} GMT",
					style = MaterialTheme.typography.bodySmall,
					color = Color(0xFFE8EAF6)
				)

				Spacer(modifier = Modifier.height(8.dp))

				// Weather information
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					// Left column: Icon and description
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						WeatherIcon(description = weatherItem.weather[0].description, iconSize = 50.dp)
						Text(
							text = weatherItem.weather[0].description.capitalize(),
							style = MaterialTheme.typography.bodySmall,
							fontWeight = FontWeight.Medium,
							color = Color.White
						)
					}

					// Middle column: Temperature
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "${weatherItem.main.temp.toInt()}°C",
							style = MaterialTheme.typography.headlineMedium,
							fontWeight = FontWeight.Bold,
							color = Color.White
						)
						Text(
							text = "Feels like ${weatherItem.main.feels_like.toInt()}°C",
							style = MaterialTheme.typography.bodySmall,
							color = Color.White
						)
					}

					// Right column: Wind and Humidity
					Column(
						horizontalAlignment = Alignment.End
					) {
						Text(
							text = "Wind: ${weatherItem.wind.speed} km/h",
							style = MaterialTheme.typography.bodySmall,
							color = Color.White
						)
						Text(
							text = "Humidity: ${weatherItem.main.humidity}%",
							style = MaterialTheme.typography.bodySmall,
							color = Color.White
						)
					}
				}
			}
		}
	}
}
