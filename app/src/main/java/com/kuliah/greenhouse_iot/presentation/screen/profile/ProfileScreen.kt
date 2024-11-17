package com.kuliah.greenhouse_iot.presentation.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.AktuatorData
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.model.SetPointData
import com.kuliah.greenhouse_iot.presentation.viewmodel.history.HistoryViewModel

@Composable
fun ProfileScreen(histortyViewModel: HistoryViewModel = hiltViewModel()) {

	val monitoringHistory by histortyViewModel.monitoringHistory.observeAsState(emptyList())
	val aktuatorHistory by histortyViewModel.aktuatorHistory.observeAsState(emptyList())
	val setPointHistory by histortyViewModel.setPointHistory.observeAsState(emptyList())

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		item {
			Text(text = "Monitoring History", style = MaterialTheme.typography.bodySmall)
		}
		items(monitoringHistory) { data ->
			MonitoringHistoryItem(data = data)
		}

		item {
			Spacer(modifier = Modifier.height(16.dp))
			Text(text = "Aktuator History", style = MaterialTheme.typography.bodySmall)
		}
		items(aktuatorHistory) { data ->
			AktuatorHistoryItem(data = data)
		}

		item {
			Spacer(modifier = Modifier.height(16.dp))
			Text(text = "SetPoint History", style = MaterialTheme.typography.bodySmall)
		}
		items(setPointHistory) { data ->
			SetPointHistoryItem(data = data)
		}
	}
}


@Composable
fun MonitoringHistoryItem(data: MonitoringData) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		//		elevation = 4.dp
	) {
		Column(modifier = Modifier.padding(8.dp)) {
			Text(text = "Timestamp: ${data.timestamp}", style = MaterialTheme.typography.bodyMedium)
			Text(text = "Water PPM: ${data.waterppm}", style = MaterialTheme.typography.bodyMedium)
			Text(
				text = "Water Temp: ${data.watertemp}",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(text = "Air Temp: ${data.airtemp}", style = MaterialTheme.typography.bodyMedium)
			Text(text = "pH: ${data.waterph}", style = MaterialTheme.typography.bodyMedium)
			Text(text = "TDS: ${data.airhum}", style = MaterialTheme.typography.bodyMedium)
		}
	}
}

@Composable
fun AktuatorHistoryItem(data: AktuatorData) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		//		elevation = 4.dp
	) {
		Column(modifier = Modifier.padding(8.dp)) {
			Text(text = "Timestamp: ${data.timestamp}", style = MaterialTheme.typography.bodyMedium)
			Text(
				text = "Nutrisi: ${data.actuator_nutrisi}",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(text = "pH Up: ${data.actuator_ph_up}", style = MaterialTheme.typography.bodyMedium)
			Text(
				text = "pH Down: ${data.actuator_ph_down}",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(
				text = "Pompa Utama: ${data.actuator_pompa_utama_1}",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(
				text = "Pompa Cadangan: ${data.actuator_pompa_utama_2}",
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

@Composable
fun SetPointHistoryItem(data: SetPointData) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		//		elevation = 4.dp
	) {
		Column(modifier = Modifier.padding(8.dp)) {
			Text(text = "Timestamp: ${data.timestamp}", style = MaterialTheme.typography.bodyMedium)
			Text(
				text = "Setpoint Water Temp: ${data.watertemp}",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(
				text = "Setpoint Air Temp: ${data.waterph}",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(
				text = "Setpoint pH: ${data.waterppm}",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(
				text = "Setpoint TDS: ${data.profile}",
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}