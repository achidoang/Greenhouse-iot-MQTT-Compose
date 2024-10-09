package com.kuliah.greenhouse_iot.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.presentation.common.NoInternetComponent
import com.kuliah.greenhouse_iot.presentation.viewmodel.home.HomeScreenViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.mqtt.MqttViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	modifier: Modifier,
	navController: NavHostController,
	darkTheme: Boolean,
	mqttViewModel: MqttViewModel = hiltViewModel(),

) {
	val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()



	val monitoringData by mqttViewModel.monitoringData.collectAsState(initial = null)
	val errorState by mqttViewModel.errorState.collectAsState(initial = null)

	LaunchedEffect(Unit) {
		// Tempat untuk logika reconnect otomatis, jika diperlukan
	}

	// Menampilkan pesan error jika ada
	errorState?.let {
		if (it.isNotEmpty()) {
			ErrorDialog(message = it)
		}
	}

	// Box utama yang mengatur tampilan
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		if (errorState != null) {
			Text(text = errorState ?: "Unknown Error", color = Color.Red)
		} else if (monitoringData != null) {
			// Menampilkan grid ketika data tersedia
			HomeScreenGrid(monitoringData!!)
		} else {
			CircularProgressIndicator()
			Text(text = "Connecting to MQTT...", color = Color.Blue)
		}
	}
}

@Composable
fun HomeScreenGrid(data: MonitoringData) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		Text(
			text = "Greenhouse v1.0",
			style = MaterialTheme.typography.titleLarge,
			modifier = Modifier.align(Alignment.CenterHorizontally)
		)
		Spacer(modifier = Modifier.height(8.dp))

		// Grid baris pertama (Water Temperature, formasi 1 kolom)
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			StatCard(
				title = "Water Temp",
				value = "${data.watertemp ?: "-"}°C",
				backgroundColor = Color(0xFFB3E5FC),
				modifier = Modifier.fillMaxWidth() // Satu kolom
			)
		}

		// Grid baris kedua (Water PPM dan Water pH, formasi 2 kolom)
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			StatCard(
				title = "Water PPM",
				value = "${data.waterppm ?: "-"} ppm",
				backgroundColor = Color(0xFFDCEDC8),
				modifier = Modifier.weight(1f) // Kolom pertama
			)
			StatCard(
				title = "Water pH",
				value = "${data.waterph ?: "-"}",
				backgroundColor = Color(0xFFFFF59D),
				modifier = Modifier.weight(1f) // Kolom kedua
			)
		}

		// Grid baris ketiga (Air Temperature dan Air Humidity, formasi 2 kolom)
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			StatCard(
				title = "Air Temp",
				value = "${data.airtemp ?: "-"}°C",
				backgroundColor = Color(0xFFFFCC80),
				modifier = Modifier.weight(1f) // Kolom pertama
			)
			StatCard(
				title = "Air Humidity",
				value = "${data.airhum ?: "-"}%",
				backgroundColor = Color(0xFFB3E5FC),
				modifier = Modifier.weight(1f) // Kolom kedua
			)
		}

		// Last update text
		Text(
			text = "Last update: 16:28:16 WIB - 23/08/2024", // ganti dengan data real-time
			style = MaterialTheme.typography.bodySmall,
			modifier = Modifier.align(Alignment.CenterHorizontally),
			color = Color.Gray
		)
	}
}

@Composable
fun StatCard(title: String, value: String, backgroundColor: Color, modifier: Modifier = Modifier) {
	Card(
		modifier = modifier
			.height(120.dp),
		colors = CardDefaults.cardColors(backgroundColor)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			verticalArrangement = Arrangement.SpaceBetween
		) {
			Text(text = title, style = MaterialTheme.typography.bodyMedium)
			Text(text = value, style = MaterialTheme.typography.labelLarge)
		}
	}
}

@Composable
fun ErrorDialog(message: String) {
	AlertDialog(
		onDismissRequest = { /* Handle dismiss */ },
		title = { Text(text = "Error") },
		text = { Text(text = message) },
		confirmButton = {
			TextButton(onClick = { /* Handle confirm */ }) {
				Text(text = "OK")
			}
		}
	)
}