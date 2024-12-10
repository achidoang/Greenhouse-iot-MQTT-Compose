package com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileUiState
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
	profileId: Int,
	onProfileUpdated: () -> Unit,
	viewModel: ProfileViewModel = hiltViewModel(),
	navController: NavController
) {
	val uiState by viewModel.uiState.collectAsState()

	// Saat pertama kali dipanggil, muat profil berdasarkan ID
	LaunchedEffect(key1 = profileId) {
		viewModel.loadProfiles()
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Edit Profile") },
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
						Icon(Icons.Default.ArrowBack, contentDescription = "Back")
					}

				},
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = MaterialTheme.colorScheme.surface
			)
		}
	) { paddingValues ->
		when (uiState) {
			is ProfileUiState.Loading -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(paddingValues),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}
			}

			is ProfileUiState.Error -> {
				val errorMessage = (uiState as ProfileUiState.Error).message
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(paddingValues),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = "Error: $errorMessage",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.error
					)
				}
			}

			is ProfileUiState.Success -> {
				val profiles = (uiState as ProfileUiState.Success).profiles
				val profile = profiles.find { it.id == profileId }

				if (profile != null) {
					var watertemp by remember { mutableStateOf(profile.watertemp) }
					var waterppm by remember { mutableStateOf(profile.waterppm) }
					var waterph by remember { mutableStateOf(profile.waterph) }
					var profileName by remember { mutableStateOf(profile.profile) }

					Column(
						modifier = Modifier
							.fillMaxSize()
							.padding(paddingValues)
							.padding(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						Text(
							text = "Edit Profile Details",
							style = MaterialTheme.typography.titleLarge,
							color = MaterialTheme.colorScheme.primary
						)

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

						// Update Button
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
							modifier = Modifier.fillMaxWidth(),
							colors = ButtonDefaults.buttonColors(
								containerColor = MaterialTheme.colorScheme.primary,
								contentColor = MaterialTheme.colorScheme.onPrimary
							)
						) {
							Text("Update Profile")
						}
					}
				} else {
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(paddingValues),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = "Profile not found.",
							style = MaterialTheme.typography.bodyMedium,
							color = MaterialTheme.colorScheme.onSurface
						)
					}
				}
			}
		}
	}
}

@Composable
fun CustomInputWithSlider(
	label: String,
	value: Double,
	onValueChange: (Double) -> Unit,
	valueRange: ClosedFloatingPointRange<Double>,
	unit: String
) {
	Column {
		Text(
			text = label,
			style = MaterialTheme.typography.bodyMedium
		)
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			// OutlinedTextField hanya menampilkan 2 angka di belakang koma
			OutlinedTextField(
				value = String.format("%.2f", value), // Format nilai menjadi dua angka di belakang koma
				onValueChange = {
					// Normalisasi input: ganti koma (,) dengan titik (.)
					val normalizedInput = it.replace(',', '.')
					val input = normalizedInput.toDoubleOrNull() // Parsing input menjadi Double
					if (input != null && input in valueRange) {
						onValueChange(input) // Memanggil lambda untuk memperbarui nilai di state
					}
				},
//				label = { Text(label) },
				keyboardOptions = KeyboardOptions.Default.copy(
					keyboardType = KeyboardType.Number
				),
				modifier = Modifier.weight(1f)
			)

			// Slider untuk mengubah nilai
			Slider(
				value = value.toFloat(),
				onValueChange = { onValueChange(it.toDouble()) },
				valueRange = valueRange.start.toFloat()..valueRange.endInclusive.toFloat(),
				modifier = Modifier.weight(2f)
			)
		}
	}
}
