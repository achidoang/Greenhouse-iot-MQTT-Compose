package com.kuliah.greenhouse_iot.presentation.screen.home

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuliah.greenhouse_iot.R
import com.kuliah.greenhouse_iot.data.model.weather.WeatherItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecastSection(
	modifier: Modifier = Modifier,
	forecasts: List<WeatherItem>
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 12.dp, vertical = 8.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.tertiaryContainer
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
	) {
		Column(
			modifier = Modifier
				.padding(12.dp)
		) {
			Text(
				text = "Hourly Forecast",
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.SemiBold,
				modifier = Modifier.padding(bottom = 8.dp),
				color = MaterialTheme.colorScheme.onSurface
			)

			LazyRow(
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			) {
				items(forecasts) { forecast ->
					CompactHourlyForecastItem(forecast)
				}
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompactHourlyForecastItem(
	weatherItem: WeatherItem
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Text(
			text = formatTime(weatherItem.dt_txt),
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onBackground
		)

		WeatherIcon(
			description = weatherItem.weather[0].description,
			iconSize = 32.dp
		)

		Text(
			text = "${weatherItem.main.temp.toInt()}°",
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}

@Composable
fun WeatherIcon(description: String, iconSize: Dp = 32.dp) {
	val iconRes = getWeatherIconResource(description)
	Image(
		painter = painterResource(id = iconRes),
		contentDescription = description,
		modifier = Modifier.size(iconSize)
	)
}

@DrawableRes
fun getWeatherIconResource(description: String): Int {
	return when (description.lowercase()) {
		"clear sky" -> R.drawable.ic_clear_day
		"few clouds" -> R.drawable.ic_few_cloudy_day
		"scattered clouds" -> R.drawable.ic_scattered_cloudy_day
		"broken clouds" -> R.drawable.ic_broken_cloudy
		"light rain" -> R.drawable.ic_light_rain
		"overcast clouds" -> R.drawable.ic_overcast_cloudy
		else -> R.drawable.ic_clear_day // default icon
	}
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dateTimeString: String): String {
	val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
	return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}
