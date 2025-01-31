package com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
	onProfileCreated: () -> Unit,
	viewModel: ProfileViewModel = hiltViewModel(),
	navController: NavController
) {
	var watertemp by remember { mutableStateOf(25.0) }
	var waterppm by remember { mutableStateOf(100.0) }
	var waterph by remember { mutableStateOf(7.0) }
	var profileName by remember { mutableStateOf("") }
	var errorMessage by remember { mutableStateOf("") }

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Create Profile") },
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.Default.ArrowBack, contentDescription = "Back")
					}
				},
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = MaterialTheme.colorScheme.surface
			)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			Text(
				text = "Create Profile Details",
				style = MaterialTheme.typography.titleLarge,
				color = MaterialTheme.colorScheme.primary
			)

			// Error Message
			if (errorMessage.isNotEmpty()) {
				Text(
					text = errorMessage,
					color = MaterialTheme.colorScheme.error,
					style = MaterialTheme.typography.bodyMedium
				)
			}

			// Profile Name
			OutlinedTextField(
				value = profileName,
				onValueChange = { profileName = it },
				label = { Text("Profile Name") },
				modifier = Modifier.fillMaxWidth(),
				colors = TextFieldDefaults.outlinedTextFieldColors(
					focusedBorderColor = MaterialTheme.colorScheme.primary,
					unfocusedBorderColor = MaterialTheme.colorScheme.outline
				)
			)

			// Water Temperature
			CustomInputWithSlider(
				label = "Water Temperature",
				value = watertemp,
				onValueChange = { watertemp = it },
				valueRange = 0.0..100.0,
				unit = "°C"
			)

			// Water PPM
			CustomInputWithSlider(
				label = "Water PPM",
				value = waterppm,
				onValueChange = { waterppm = it },
				valueRange = 0.0..1000.0,
				unit = "ppm"
			)

			// Water PH
			CustomInputWithSlider(
				label = "Water PH",
				value = waterph,
				onValueChange = { waterph = it },
				valueRange = 0.0..14.0,
				unit = ""
			)

			// Create Button
			Button(
				onClick = {
					val newProfile = Profile(
						id = 0, // ID akan diatur oleh server
						watertemp = watertemp,
						waterppm = waterppm,
						waterph = waterph,
						profile = profileName,
						status = 0,
						timestamp = "" // Akan diisi oleh server
					)
					viewModel.createProfile(newProfile)
					onProfileCreated()
				},
				modifier = Modifier.fillMaxWidth(),
				enabled = profileName.isNotBlank(),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primary,
					contentColor = MaterialTheme.colorScheme.onPrimary
				)
			) {
				Text("Create Profile")
			}
		}
	}
}
