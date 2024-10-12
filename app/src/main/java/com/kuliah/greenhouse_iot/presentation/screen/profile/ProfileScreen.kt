package com.kuliah.greenhouse_iot.presentation.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.presentation.screen.home.ActuatorStatusGrid
import com.kuliah.greenhouse_iot.presentation.viewmodel.actuator.ActuatorViewModel

@Composable
fun ProfileScreen(actuatorViewModel: ActuatorViewModel = hiltViewModel()) {
	Column {
		// Isi halaman profil
		Text(text = "Ini adalah halaman profil")
		val actuatorStatus by actuatorViewModel.actuatorStatus.collectAsState()
		ActuatorStatusGrid(actuatorStatus)
	}
}