package com.kuliah.greenhouse_iot.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.actuator.AktuatorViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.auth.AuthViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring.MonitoringViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.setPoint.SetPointViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	monitoringViewModel: MonitoringViewModel = hiltViewModel(),
	aktuatorViewModel: AktuatorViewModel = hiltViewModel(),
	setPointViewModel: SetPointViewModel = hiltViewModel(),
	authViewModel: AuthViewModel = hiltViewModel()
	//	darkTheme: Boolean,

) {
	val monitoringData = monitoringViewModel.monitoringData.collectAsState().value
	val aktuatorData = aktuatorViewModel.aktuatorData.collectAsState().value
	val setPointData = setPointViewModel.setPointData.collectAsState().value
	Log.d("HomeScreen", "Aktuator data in UI: $aktuatorData")
	Log.d("HomeScreen", "Monitoring data in UI: $monitoringData")
	Log.d("HomeScreen", "SetPoint data in UI: $setPointData")



	// Main UI layout
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {


		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
		) {
			Text(text = "Data Monitoring", style = MaterialTheme.typography.labelLarge)

			if (monitoringData != null) {
				Text(text = "Suhu Air: ${monitoringData.watertemp} °C")
				Text(text = "PPM Air: ${monitoringData.waterppm} ppm")
				Text(text = "pH Air: ${monitoringData.waterph}")
				Text(text = "Suhu Udara: ${monitoringData.airtemp} °C")
				Text(text = "Kelembapan Udara: ${monitoringData.airhum}%")
				Text(text = "Timestamp: ${monitoringData.timestamp}")
			} else {
				Text(text = "Mengambil data...")
			}
			Spacer(modifier = Modifier.height(16.dp))

			// Aktuator Data Display
			Text(text = "Data Aktuator", style = MaterialTheme.typography.labelLarge)
			if (aktuatorData != null) {
				Text("Nutrisi: ${if (aktuatorData.actuator_nutrisi) "On" else "Off"}")
				Text("pH Up: ${if (aktuatorData.actuator_ph_up) "On" else "Off"}")
				Text("pH Down: ${if (aktuatorData.actuator_ph_down) "On" else "Off"}")
				Text("Air Baku: ${if (aktuatorData.actuator_air_baku) "On" else "Off"}")
				Text("Pompa Utama 1: ${if (aktuatorData.actuator_pompa_utama_1) "On" else "Off"}")
				Text("Pompa Utama 2: ${if (aktuatorData.actuator_pompa_utama_2) "On" else "Off"}")
				Text("Timestamp: ${aktuatorData.timestamp}")
			} else {
				Text(text = "Loading Aktuator Data...")
			}

			Spacer(modifier = Modifier.height(16.dp))

			// SetPoint Data Display
			Text(text = "Data SetPoint", style = MaterialTheme.typography.labelLarge)
			if (setPointData != null) {
				Text(text = "SetPoint Suhu Air: ${setPointData.watertemp} °C")
				Text(text = "SetPoint PPM Air: ${setPointData.waterppm} ppm")
				Text(text = "SetPoint pH Air: ${setPointData.waterph}")
				Text(text = "Profile: ${setPointData.profile}")
				Text(text = "Timestamp: ${setPointData.timestamp}")
			} else {
				Text(text = "Loading SetPoint Data...")
			}

			// Logout Button
			Button(
				onClick = {
					authViewModel.logout()
					navController.navigate(Route.Login.destination) {
						popUpTo(Route.Home.destination) { inclusive = true }
					}
				},
				modifier = Modifier.fillMaxWidth()
			) {
				Text("Logout")
			}

		}

	}
}



@Composable
fun HomeScreenGrid(data: MonitoringData) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp),
		verticalArrangement = Arrangement.spacedBy(10.dp)
	) {
		Spacer(modifier = Modifier.height(5.dp))
		Text(
			text = "Real Time Monitoring",
			style = MaterialTheme.typography.titleLarge,
			modifier = Modifier.align(Alignment.CenterHorizontally)
		)

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
			.height(100.dp),
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



//@Composable
//fun ActuatorStatusGrid(actuatorStatus: ActuatorStatus) {
//	Column(
//		modifier = Modifier
//			.fillMaxWidth()
//			.padding(16.dp)
//	) {
//		Text(
//			text = "Status Aktuator",
//			style = MaterialTheme.typography.titleSmall,
//			modifier = Modifier.align(Alignment.CenterHorizontally)
//		)
//
//		Spacer(modifier = Modifier.height(6.dp))
//
//		// Grid untuk aktuator status
//		LazyVerticalGrid(
//			columns = GridCells.Fixed(2), // Membuat grid dengan 2 kolom
//			verticalArrangement = Arrangement.spacedBy(4.dp),
//			horizontalArrangement = Arrangement.spacedBy(4.dp),
//			modifier = Modifier.fillMaxWidth()
//		) {
//			item {
//				ActuatorStatusCard(
//					title = "Aktuator Nutrisi",
//					backgroundColor = Color(0xFFFFF59D),
//					status = if (actuatorStatus.actuator_nutrisi == 1) "ON" else "OFF"
//				)
//			}
//			item {
//				ActuatorStatusCard(
//					title = "Aktuator pH Up",
//					backgroundColor = Color(0xFFFFCC80),
//					status = if (actuatorStatus.actuator_ph_up == 1) "ON" else "OFF"
//				)
//			}
//			item {
//				ActuatorStatusCard(
//					title = "Aktuator pH Down",
//					backgroundColor = Color(0xFFFFCC80),
//					status = if (actuatorStatus.actuator_ph_down == 1) "ON" else "OFF"
//				)
//			}
//			item {
//				ActuatorStatusCard(
//					title = "Aktuator Air Baku",
//					backgroundColor = Color(0xFFDCEDC8),
//					status = if (actuatorStatus.actuator_air_baku == 1) "ON" else "OFF"
//				)
//			}
//			item {
//				ActuatorStatusCard(
//					title = "Aktuator Pompa 1",
//					backgroundColor = Color(0xFFB3E5FC),
//					status = if (actuatorStatus.actuator_pompa_utama_1 == 1) "ON" else "OFF"
//				)
//			}
//			item {
//				ActuatorStatusCard(
//					title = "Aktuator Pomp 2",
//					backgroundColor = Color(0xFFFFF59D),
//					status = if (actuatorStatus.actuator_pompa_utama_2 == 1) "ON" else "OFF"
//				)
//			}
//		}
//	}
//}

//@Composable
//fun ActuatorStatusCard(title: String, status: String, backgroundColor: Color) {
//	Card(
//		modifier = Modifier
//			.fillMaxWidth()
//			.height(80.dp),
//		//		elevation = 4.dp,
//				colors = CardDefaults.cardColors(backgroundColor)
//	) {
//		Column(
//			modifier = Modifier
//				.fillMaxSize()
//				.padding(12.dp),
//			verticalArrangement = Arrangement.SpaceBetween
//		) {
//			Text(text = title, style = MaterialTheme.typography.bodyMedium)
//			Text(
//				text = status,
//				style = MaterialTheme.typography.labelSmall,
//				color = if (status == "ON") Color.DarkGray else Color.Red
//			)
//		}
//	}
//}


