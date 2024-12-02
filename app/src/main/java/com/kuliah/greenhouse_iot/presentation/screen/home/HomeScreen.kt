package com.kuliah.greenhouse_iot.presentation.screen.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.kuliah.greenhouse_iot.R
import com.kuliah.greenhouse_iot.data.model.subscribe.MonitoringData
import com.kuliah.greenhouse_iot.data.model.weather.City
import com.kuliah.greenhouse_iot.data.model.weather.WeatherItem
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring.MonitoringViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.weather.WeatherViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	monitoringViewModel: MonitoringViewModel = hiltViewModel(),
	weatherViewModel: WeatherViewModel = hiltViewModel()
) {
	val weatherState by weatherViewModel.weatherState.collectAsState()
	val locationState by weatherViewModel.locationState.collectAsState()
	val showDialog = remember { mutableStateOf(false) }
	val context = LocalContext.current

	val monitoringData = monitoringViewModel.monitoringData.collectAsState().value
	val deviceStatus by monitoringViewModel.deviceStatus.collectAsState()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Monitoring Realtime") },
				actions = {
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
				},
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = MaterialTheme.colorScheme.onSurface
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = { showDialog.value = true },
				containerColor = MaterialTheme.colorScheme.primary,
				modifier = Modifier.padding(bottom = 70.dp)
			) {
				Icon(Icons.Default.Refresh, contentDescription = "Tampilkan Cuaca")
			}
		},
		content = { paddingValues ->
			Box(modifier = Modifier.padding(paddingValues)) {
				Box(
					modifier = Modifier
						.fillMaxSize()
				) {
					if (monitoringData != null) {
						Column(
							modifier = Modifier
								.fillMaxSize()
								.padding(16.dp)
						) {
							// Monitoring Section
							MonitoringSection(monitoringData, deviceStatus)

							// Cuaca di bawah ini
							weatherState?.data?.let { weatherData ->
								Column() {
									WeatherOverviewSection(city = weatherData.city, weatherItem = weatherData.list[0])
									HourlyForecastSection(forecasts = weatherData.list.take(24))
									Spacer(modifier = Modifier.height(16.dp))
								}
							} ?: run {
								Box(
									modifier = Modifier.fillMaxSize(),
									contentAlignment = Alignment.Center
								) {
									Text("Data cuaca belum tersedia. Tekan tombol untuk memuat.")
									LaunchedEffect(Unit) {
										weatherViewModel.fetchWeather() // Panggil hanya jika data belum ada
									}
								}
							}
						}
					} else {
						Box(
							modifier = Modifier.fillMaxSize(),
							contentAlignment = Alignment.Center
						) {
							LottieLoading()
						}
					}

				}

			}
		}
	)

	if (showDialog.value) {
		AlertDialog(
			onDismissRequest = { showDialog.value = false },
			title = { Text("Konfirmasi") },
			text = { Text("Apakah Anda yakin ingin menampilkan data cuaca?") },
			confirmButton = {
				Button(onClick = {
					showDialog.value = false
					requestLocationPermission(context) { latitude, longitude ->
						weatherViewModel.postLocation(latitude, longitude)
						weatherViewModel.fetchWeather()
					}
				}) {
					Text("Ya")
				}
			},
			dismissButton = {
				Button(onClick = { showDialog.value = false }) {
					Text("Tidak")
				}
			}
		)
	}
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun HomeScreen(
//	modifier: Modifier = Modifier,
//	navController: NavHostController,
//	monitoringViewModel: MonitoringViewModel = hiltViewModel(),
//	weatherViewModel: WeatherViewModel = hiltViewModel()
//) {
//	val monitoringData = monitoringViewModel.monitoringData.collectAsState().value
//	val deviceStatus by monitoringViewModel.deviceStatus.collectAsState()
//
//	var showDialog by remember { mutableStateOf(false) }
//
//	Scaffold(
//		topBar = {
//			TopAppBar(
//				title = { Text("Monitoring Realtime") },
//				actions = {
//					IconButton(onClick = {
//						navController.navigate(Route.Manage.destination) {
//							popUpTo(Route.Home.destination) { inclusive = false }
//						}
//					}) {
//						Icon(
//							imageVector = Icons.Default.Person2,
//							contentDescription = "Settings",
//							tint = Color.Gray
//						)
//					}
//				},
//				backgroundColor = MaterialTheme.colorScheme.background,
//				contentColor = MaterialTheme.colorScheme.onSurface
//			)
//		},
//		floatingActionButton = {
//			FloatingActionButton(
//				onClick = { showDialog = true },
//				backgroundColor = MaterialTheme.colorScheme.primary,
//				modifier = Modifier.padding(bottom = 70.dp)
//			) {
//				Icon(
//					imageVector = Icons.Default.Refresh,
//					contentDescription = "Tampilkan Cuaca",
//					tint = Color.White
//				)
//			}
//		},
//	) { innerPadding ->
//		Box(
//			modifier = Modifier
//				.fillMaxSize()
//				.padding(innerPadding)
//		) {
//			if (monitoringData != null) {
//				Column(
//					modifier = Modifier
//						.fillMaxSize()
//						.padding(16.dp)
//				) {
//					// Monitoring Section
//					MonitoringSection(monitoringData, deviceStatus)
//
//					// Cuaca di bawah ini
//				}
//			} else {
//				Box(
//					modifier = Modifier.fillMaxSize(),
//					contentAlignment = Alignment.Center
//				) {
//					LottieLoading()
//				}
//			}
//
//		}
//	}
//}


@Composable
fun WeatherSection(
	weatherState: WeatherResponse?,
	locationMessage: String?
) {
	Column {
		weatherState?.let { weather ->
			val currentWeather = weather.data.list[0]
			WeatherCard(
				temperature = currentWeather.main.temp.toInt(),
				minTemperature = currentWeather.main.temp_min.toInt(),
				precipitation = 22, // Ganti dengan data aktual
				humidity = currentWeather.main.humidity,
				windSpeed = currentWeather.wind.speed.toInt(),
				description = currentWeather.weather[0].description.capitalize()
			)
		}

		locationMessage?.let { message ->
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = "Location Response: $message",
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurface
			)
		}
	}
}


@Composable
fun WeatherCard(
	temperature: Int,
	minTemperature: Int,
	precipitation: Int,
	humidity: Int,
	windSpeed: Int,
	description: String
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				Brush.verticalGradient(
					colors = listOf(
						Color(0xFF4E3AAB),
						Color(0xFF6B47CC)
					)
				),
				shape = RoundedCornerShape(16.dp)
			)
			.padding(16.dp)
	) {
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			// Weather description
			Text(
				text = "Tomorrow",
				style = MaterialTheme.typography.bodyMedium.copy(
					color = Color.White.copy(alpha = 0.8f)
				)
			)
			Text(
				text = description,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.Bold
				)
			)
			Spacer(modifier = Modifier.height(8.dp))
			// Main temperature
			Text(
				text = "$temperature°",
				style = MaterialTheme.typography.displayLarge.copy(
					color = Color.White,
					fontWeight = FontWeight.Bold
				)
			)
			Text(
				text = "$minTemperature°",
				style = MaterialTheme.typography.titleMedium.copy(
					color = Color.White.copy(alpha = 0.8f)
				)
			)
			Spacer(modifier = Modifier.height(16.dp))
			// Weather icons
			Row(
				horizontalArrangement = Arrangement.SpaceEvenly,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				WeatherInfoItem(
					icon = Icons.Default.Umbrella,
					label = "Precipitation",
					value = "$precipitation%"
				)
				WeatherInfoItem(
					icon = Icons.Default.WaterDrop,
					label = "Humidity",
					value = "$humidity%"
				)
				WeatherInfoItem(
					icon = Icons.Default.Air,
					label = "Wind Speed",
					value = "${windSpeed}km/h"
				)
			}
		}
	}
}

@Composable
fun LocationSection(city: City) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
			.clip(RoundedCornerShape(16.dp))
			.background(Brush.horizontalGradient(listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC))))
			.padding(16.dp)
	) {
		Column {
			Text(
				text = "${city.name}, ${city.country}",
				style = MaterialTheme.typography.headlineSmall,
				fontWeight = FontWeight.Bold,
				color = Color.White
			)
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				text = "Zona waktu: ${city.timezone / 3600} GMT",
				style = MaterialTheme.typography.bodyMedium,
				color = Color(0xFFE8EAF6)
			)
		}
	}
}

@Composable
fun CurrentWeatherSection(weatherItem: WeatherItem) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
			.clip(RoundedCornerShape(16.dp)),
		colors = CardDefaults.cardColors(
			containerColor = Color.Transparent
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					Brush.verticalGradient(
						colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC))
					)
				)
				.padding(16.dp)
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.fillMaxWidth()
			) {
				WeatherIcon(description = weatherItem.weather[0].description, iconSize = 70.dp) // Ukuran ikon lebih besar

				Text(
					text = weatherItem.weather[0].description.capitalize(), // Deskripsi cuaca
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Medium,
					color = Color.White
				)

				Text(
					text = "${weatherItem.main.temp.toInt()}°C",
					style = MaterialTheme.typography.headlineLarge,
					fontWeight = FontWeight.Bold,
					color = Color.White
				)

				Text(
					text = "Feels like ${weatherItem.main.feels_like.toInt()}°C",
					style = MaterialTheme.typography.bodyMedium,
					color = Color.White
				)

				Spacer(modifier = Modifier.height(8.dp))

				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
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


@Composable
fun WeatherInfoItem(icon: ImageVector, label: String, value: String) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Icon(
			imageVector = icon,
			contentDescription = label,
			tint = Color.White,
			modifier = Modifier.size(24.dp)
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodySmall.copy(
				color = Color.White
			)
		)
		Text(
			text = label,
			style = MaterialTheme.typography.bodySmall.copy(
				color = Color.White.copy(alpha = 0.8f)
			)
		)
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

@Composable
fun MonitoringSection(
	monitoringData: MonitoringData,
	deviceStatus: String
) {
	Column {
		Text(
			text = "Nutrisi Air",
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurface
		)

		// Display PPM
		Row(
			modifier = Modifier.padding(vertical = 8.dp),
			verticalAlignment = Alignment.Bottom
		) {
			Text(
				text = "${monitoringData.waterppm.toInt()}",
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.headlineMedium.copy(
					fontWeight = FontWeight.Bold
				)
			)
			Text(
				text = " ppm",
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
			)
		}

		// Progress Bar
		LinearProgressIndicator(
			progress = monitoringData.waterppm / 1000f,
			modifier = Modifier
				.fillMaxWidth()
				.height(8.dp)
				.clip(RoundedCornerShape(4.dp)),
			color = Color(0xFF4CAF50),
			trackColor = Color(0xFFE0E0E0)
		)

		Spacer(modifier = Modifier.height(16.dp))

		// Monitoring Cards Grid
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
					value = monitoringData.airhum,
					maxValue = 100f,
					label = "Kelembaban",
					unit = "%",
					color = Color(0xFF9C27B0)
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
		}
	}
}


fun requestLocationPermission(context: Context, onLocationObtained: (Double, Double) -> Unit) {
	val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

	if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
		ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
	) {
		ActivityCompat.requestPermissions(
			context as Activity,
			arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
			1
		)
		return
	}

	fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
		location?.let {
			onLocationObtained(it.latitude, it.longitude)
		} ?: run {
			Toast.makeText(context, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
		}
	}
}


