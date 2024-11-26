package com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileUiState
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileListScreen(
	viewModel: ProfileViewModel = hiltViewModel(),
	navController: NavController
) {
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.loadProfiles()
		viewModel.observeRealTimeProfiles()
	}

	Scaffold(
		floatingActionButton = {
			FloatingActionButton(
				modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 70.dp),
				onClick = { navController.navigate(Route.CreateProfile.destination) },
				containerColor = MaterialTheme.colorScheme.primary
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = "Create Profile",
					tint = Color.White
				)
			}
		}
	) { padding ->
		// Existing content here
		when (uiState) {
			is ProfileUiState.Loading -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}
			}


			is ProfileUiState.Success -> {
				val profiles = (uiState as ProfileUiState.Success).profiles
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding)
						.padding(horizontal = 16.dp)
						.padding(bottom = 80.dp)
				) {
					items(profiles) { profile ->
						ProfileCard(
							profile = profile,
							onActivate = { viewModel.activateProfile(profile.id) },
							onDelete = { viewModel.deleteProfile(profile.id) },
							onEdit = { navController.navigate("${Route.EditProfile.destination}/${profile.id}") }
						)
					}
				}

			}

			is ProfileUiState.Error -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = (uiState as ProfileUiState.Error).message,
						color = MaterialTheme.colorScheme.error
					)
				}
			}
		}
	}
}


@Composable
fun ProfileCard(
	profile: Profile,
	onActivate: () -> Unit,
	onDelete: () -> Unit,
	onEdit: () -> Unit
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			// Title Row (Profile Name and Actions)
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = profile.profile,
					style = MaterialTheme.typography.headlineSmall,
					modifier = Modifier.weight(1f)
				)

				Row {
					IconButton(onClick = onEdit) {
						Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
					}

					IconButton(onClick = onDelete) {
						Icon(Icons.Default.Delete, contentDescription = "Delete")
					}
				}
			}

			// Parameters Row
			Spacer(modifier = Modifier.height(8.dp))
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.fillMaxWidth()
			) {
				ProfileParameter(icon = Icons.Default.Thermostat, label = "Temp", value = "${profile.watertemp}°C")
				ProfileParameter(icon = Icons.Default.Water, label = "PPM", value = "${profile.waterppm}")
				ProfileParameter(icon = Icons.Default.PieChart, label = "pH", value = "${profile.waterph}")
			}

			// Activate Button
			if (profile.status != "active") {
				Spacer(modifier = Modifier.height(8.dp))
				Button(
					onClick = onActivate,
					modifier = Modifier.fillMaxWidth()
				) {
					Text("Activate")
				}
			} else {
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = "Active Profile",
					color = MaterialTheme.colorScheme.primary,
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center
				)
			}
		}
	}
}

@Composable
fun ProfileParameter(icon: ImageVector, label: String, value: String) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
		Text(
			text = "$label: $value",
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}
