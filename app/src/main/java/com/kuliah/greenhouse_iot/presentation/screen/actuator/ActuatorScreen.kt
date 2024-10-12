package com.kuliah.greenhouse_iot.presentation.screen.actuator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.presentation.viewmodel.actuator.ActuatorViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.mqtt.MqttViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActuatorScreen(viewModel: ActuatorViewModel = hiltViewModel()) {
	val actuatorStatus by viewModel.actuatorStatus.collectAsState()

	LazyVerticalGrid(
		columns = GridCells.Fixed(2), // Untuk menampilkan dalam grid 2 kolom
		modifier = Modifier.fillMaxSize(),
		contentPadding = PaddingValues(16.dp)
	) {
		// List of actuators with corresponding switch buttons
		items(6) { index ->
			when (index) {
				0 -> ActuatorSwitchItem(
					label = "Nutrisi",
					checked = actuatorStatus.actuator_nutrisi == 1,
					onCheckedChange = { isChecked ->
						viewModel.toggleActuator("actuator_nutrisi", if (isChecked) 1 else 0)
						viewModel.confirmEdit()
					}
				)
				1 -> ActuatorSwitchItem(
					label = "pH Up",
					checked = actuatorStatus.actuator_ph_up == 1,
					onCheckedChange = { isChecked ->
						viewModel.toggleActuator("actuator_ph_up", if (isChecked) 1 else 0)
						viewModel.confirmEdit()
					}
				)
				2 -> ActuatorSwitchItem(
					label = "pH Down",
					checked = actuatorStatus.actuator_ph_down == 1,
					onCheckedChange = { isChecked ->
						viewModel.toggleActuator("actuator_ph_down", if (isChecked) 1 else 0)
						viewModel.confirmEdit()
					}
				)
				3 -> ActuatorSwitchItem(
					label = "Air Baku",
					checked = actuatorStatus.actuator_air_baku == 1,
					onCheckedChange = { isChecked ->
						viewModel.toggleActuator("actuator_air_baku", if (isChecked) 1 else 0)
						viewModel.confirmEdit()
					}
				)
				4 -> ActuatorSwitchItem(
					label = "Pompa Utama 1",
					checked = actuatorStatus.actuator_pompa_utama_1 == 1,
					onCheckedChange = { isChecked ->
						viewModel.toggleActuator("actuator_pompa_utama_1", if (isChecked) 1 else 0)
						viewModel.confirmEdit()
					}
				)
				5 -> ActuatorSwitchItem(
					label = "Pompa Utama 2",
					checked = actuatorStatus.actuator_pompa_utama_2 == 1,
					onCheckedChange = { isChecked ->
						viewModel.toggleActuator("actuator_pompa_utama_2", if (isChecked) 1 else 0)
						viewModel.confirmEdit()
					}
				)
			}
		}
	}
}

@Composable
fun ActuatorSwitchItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth()
	) {
		Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Bold)
		Spacer(modifier = Modifier.height(8.dp))
		Switch(
			checked = checked,
			onCheckedChange = onCheckedChange,
			modifier = Modifier
				.scale(1.5f)
				.padding(8.dp)
		)
	}
}

