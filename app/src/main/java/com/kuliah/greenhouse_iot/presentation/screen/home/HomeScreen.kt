package com.kuliah.greenhouse_iot.presentation.screen.home

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
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

	val monitoringData by monitoringViewModel.monitoringData.collectAsState()
	val deviceStatus by monitoringViewModel.deviceStatus.collectAsState()

	LaunchedEffect(Unit) {
		monitoringViewModel.startMonitoringService()
		weatherViewModel.startPeriodicWeatherUpdates()
	}

	DisposableEffect(Unit) {
		onDispose {
			monitoringViewModel.stopMonitoringService()
			weatherViewModel.stopPeriodicWeatherUpdates()
		}
	}

	val headColor = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground
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
							tint = textColor
						)
					}
				},
				modifier = Modifier.height(50.dp), // Reduced height
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = headColor
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = { showDialog.value = true },
				containerColor = MaterialTheme.colorScheme.primary,
				modifier = Modifier.padding(bottom = 75.dp)
			) {
				Icon(Icons.Default.MyLocation, contentDescription = "Tampilkan Cuaca")
			}
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(horizontal = 12.dp, vertical = 8.dp) // Reduced padding
			) {
				// Monitoring Section
				monitoringData?.let { data ->
					CompactMonitoringSection(data, deviceStatus)
					Spacer(modifier = Modifier.height(8.dp))
				}

				// Weather Section with reduced spacing
				CompactWeatherSection(weatherState, weatherViewModel)
			}

			// Loading indicator for monitoring data
			if (monitoringData == null) {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}
			}
		}
	}

	if (showDialog.value) {
		AlertDialog(
			onDismissRequest = { showDialog.value = false },
			title = { Text("Konfirmasi") },
			text = { Text("Apakah Anda yakin ingin menampilkan data cuaca di lokasi ini?") },
			confirmButton = {
				Button(onClick = {
					showDialog.value = false
					requestLocationPermission(context) { latitude, longitude ->
						weatherViewModel.postLocation(latitude, longitude)
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


