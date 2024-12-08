package com.kuliah.greenhouse_iot.presentation.screen.home

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuliah.greenhouse_iot.R
import com.kuliah.greenhouse_iot.data.model.weather.WeatherItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompactHourlyForecastSection(
	modifier: Modifier = Modifier,
	forecasts: List<WeatherItem>
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(bottom = 8.dp)
	) {
		Text(
			text = "Hourly Forecast",
			style = MaterialTheme.typography.titleSmall,
			fontWeight = FontWeight.SemiBold,
			modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
			color = MaterialTheme.colorScheme.onBackground
		)

		LazyRow(
			contentPadding = PaddingValues(horizontal = 12.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			items(forecasts) { forecast ->
				CompactHourlyForecastItem(forecast)
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
		verticalArrangement = Arrangement.spacedBy(4.dp),
		modifier = Modifier
			.width(60.dp)
			.background(
				color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
				shape = RoundedCornerShape(12.dp)
			)
			.padding(vertical = 8.dp, horizontal = 4.dp)
	) {
		Text(
			text = formatTime(weatherItem.dt_txt),
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)

		WeatherIcon(
			description = weatherItem.weather[0].description,
			iconSize = 32.dp
		)

		Text(
			text = "${weatherItem.main.temp.toInt()}°",
			style = MaterialTheme.typography.bodyMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}



@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dateTimeString: String): String {
	val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
	return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
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
		"few clouds" -> R.drawable.ic_few_cloudy
		"scattered clouds" -> R.drawable.ic_scattered_cloudy_day
		"broken clouds" -> R.drawable.ic_broken_cloudy
		"light rain" -> R.drawable.ic_light_rain
		"moderate rain" -> R.drawable.ic_moderate_rain
		"overcast clouds" -> R.drawable.ic_overcast_cloudy
		else -> R.drawable.ic_clear_day // default icon
	}
}


