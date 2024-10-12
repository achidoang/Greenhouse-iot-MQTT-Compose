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
import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.presentation.common.NoInternetComponent
import com.kuliah.greenhouse_iot.presentation.viewmodel.actuator.ActuatorViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.home.HomeScreenViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.mqtt.MqttViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	modifier: Modifier,
	navController: NavHostController,
	darkTheme: Boolean,
	mqttViewModel: MqttViewModel = hiltViewModel(),
	actuatorViewModel: ActuatorViewModel = hiltViewModel()

) {
	val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()

	val monitoringData by mqttViewModel.monitoringData.collectAsState(initial = null)
	val errorState by mqttViewModel.errorState.collectAsState(initial = null)
	var showErrorDialog by remember { mutableStateOf(false) }
	val actuatorStatus by actuatorViewModel.actuatorStatus.collectAsState()

	LaunchedEffect(errorState) {
		if (errorState != null && errorState!!.isNotEmpty()) {
			showErrorDialog = true
		}
	}

	// Menampilkan ErrorDialog jika showErrorDialog true
	if (showErrorDialog) {
		ErrorDialog(
			message = errorState ?: "",
			onDismiss = {
				showErrorDialog = false
			} // Mengatur state jadi false setelah dialog di-dismiss
		)
	}

	// Box utama yang mengatur tampilan
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		// Jika ada error yang relevan seperti koneksi gagal, tampilkan pesan error
		errorState?.let { error ->
			if (error == "Failed to connect to MQTT broker") {
				Text(text = error, color = Color.Red)
			}
		}

		// Jika tidak ada error dan data monitoring tersedia, tampilkan grid data
		if (monitoringData != null) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.Top
			) {
				HomeScreenGrid(monitoringData!!)
//				Spacer(modifier = Modifier.height(20.dp))
				ActuatorStatusGrid(actuatorStatus)
			}

		} else {
			CircularProgressIndicator()
			Text(text = "Connecting to MQTT...", color = Color.Blue)
		}
	}
}

@Composable
fun ActuatorStatusGrid(actuatorStatus: ActuatorStatus) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Text(
			text = "Status Aktuator",
			style = MaterialTheme.typography.titleSmall,
			modifier = Modifier.align(Alignment.CenterHorizontally)
		)

		Spacer(modifier = Modifier.height(6.dp))

		// Grid untuk aktuator status
		LazyVerticalGrid(
			columns = GridCells.Fixed(2), // Membuat grid dengan 2 kolom
			verticalArrangement = Arrangement.spacedBy(4.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			item {
				ActuatorStatusCard(
					title = "Aktuator Nutrisi",
					backgroundColor = Color(0xFFFFF59D),
					status = if (actuatorStatus.actuator_nutrisi == 1) "ON" else "OFF"
				)
			}
			item {
				ActuatorStatusCard(
					title = "Aktuator pH Up",
					backgroundColor = Color(0xFFFFCC80),
					status = if (actuatorStatus.actuator_ph_up == 1) "ON" else "OFF"
				)
			}
			item {
				ActuatorStatusCard(
					title = "Aktuator pH Down",
					backgroundColor = Color(0xFFFFCC80),
					status = if (actuatorStatus.actuator_ph_down == 1) "ON" else "OFF"
				)
			}
			item {
				ActuatorStatusCard(
					title = "Aktuator Air Baku",
					backgroundColor = Color(0xFFDCEDC8),
					status = if (actuatorStatus.actuator_air_baku == 1) "ON" else "OFF"
				)
			}
			item {
				ActuatorStatusCard(
					title = "Aktuator Pompa Utama 1",
					backgroundColor = Color(0xFFB3E5FC),
					status = if (actuatorStatus.actuator_pompa_utama_1 == 1) "ON" else "OFF"
				)
			}
			item {
				ActuatorStatusCard(
					title = "Aktuator Pompa Utama 2",
					backgroundColor = Color(0xFFFFF59D),
					status = if (actuatorStatus.actuator_pompa_utama_2 == 1) "ON" else "OFF"
				)
			}
		}
	}
}

@Composable
fun ActuatorStatusCard(title: String, status: String, backgroundColor: Color) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.height(50.dp),
		//		elevation = 4.dp,
		colors = CardDefaults.cardColors(backgroundColor)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(8.dp),
			verticalArrangement = Arrangement.SpaceBetween
		) {
			Text(text = title, style = MaterialTheme.typography.bodyMedium)
			Text(
				text = status,
				style = MaterialTheme.typography.labelSmall,
				color = if (status == "ON") Color.Green else Color.Red
			)
		}
	}
}



@Composable
fun HomeScreenGrid(data: MonitoringData) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Text(
			text = "Real Time Monitoring",
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
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
	AlertDialog(
		onDismissRequest = { onDismiss() },
		title = { Text(text = "Error") },
		text = { Text(text = message) },
		confirmButton = {
			TextButton(onClick = { onDismiss() }) {
				Text(text = "OK")
			}
		}
	)
}
