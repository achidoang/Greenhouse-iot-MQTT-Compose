package com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileViewModel

@Composable
fun EditProfileScreen(
	profile: Profile,
	onProfileUpdated: () -> Unit,
	viewModel: ProfileViewModel = hiltViewModel()
) {
	var watertemp by remember { mutableStateOf(profile.watertemp) }
	var waterppm by remember { mutableStateOf(profile.waterppm) }
	var waterph by remember { mutableStateOf(profile.waterph) }
	var profileName by remember { mutableStateOf(profile.profile) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		TextField(
			value = profileName,
			onValueChange = { profileName = it },
			label = { Text("Profile Name") },
			modifier = Modifier.fillMaxWidth()
		)

		Text("Water Temperature: ${watertemp.toInt()}°C")
		Slider(
			value = watertemp.toFloat(),
			onValueChange = { watertemp = it.toDouble() },
			valueRange = 0.0f..100.0f,
			steps = 10,
			modifier = Modifier.fillMaxWidth()
		)

		Text("Water PPM: ${waterppm.toInt()}")
		Slider(
			value = waterppm.toFloat(),
			onValueChange = { waterppm = it.toDouble() },
			valueRange = 0.0f..1000.0f,
			steps = 20,
			modifier = Modifier.fillMaxWidth()
		)

		Text("Water PH: ${waterph}")
		Slider(
			value = waterph.toFloat(),
			onValueChange = { waterph = it.toDouble() },
			valueRange = 0.0f..14.0f,
			steps = 14,
			modifier = Modifier.fillMaxWidth()
		)

		Button(
			onClick = {
				val updatedProfile = profile.copy(
					watertemp = watertemp,
					waterppm = waterppm,
					waterph = waterph,
					profile = profileName
				)
				viewModel.updateProfile(profile.id, updatedProfile)
				onProfileUpdated()
			},
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Update Profile")
		}
	}
}
