package com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.mode.ModeViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileUiState
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.ProfileViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.profile.SortType

@Composable
fun ProfileListScreen(
	viewModel: ProfileViewModel = hiltViewModel(),
	modeViewModel: ModeViewModel = hiltViewModel(),
	navController: NavController
) {
	val uiState by viewModel.uiState.collectAsState()
	val profiles by viewModel.profiles.collectAsState()
	val isAutomaticMode by modeViewModel.isAutomaticMode.collectAsState(initial = true)

	val backgroundColor = MaterialTheme.colorScheme.background
	val textColor = MaterialTheme.colorScheme.onSurface

	// State untuk dialog konfirmasi
	var showDialog by remember { mutableStateOf(false) }
	var profileToDelete by remember { mutableStateOf<Int?>(null) }



	LaunchedEffect(Unit) {
		viewModel.loadProfiles()
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(backgroundColor)
		//			.padding(16.dp)
	) {
		Scaffold(
			floatingActionButton = {
				FloatingActionButton(
					onClick = { navController.navigate(Route.CreateProfile.destination) },
					containerColor = MaterialTheme.colorScheme.primary,
					modifier = Modifier.padding(bottom = 70.dp)
				) {
					Icon(
						imageVector = Icons.Default.Add,
						contentDescription = "Create Profile",
						tint = Color.White
					)
				}
			}
		) { padding ->
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
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(padding)
					) {
						// Row untuk tombol sorting
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							TextButton(onClick = { viewModel.setSortType(SortType.ALPHABETICAL) }) {
								Text("Alfabet")
							}
							TextButton(onClick = { viewModel.setSortType(SortType.TIMESTAMP) }) {
								Text("Terbaru")
							}
						}

						LazyColumn(
							modifier = Modifier
								.fillMaxWidth()
								.padding(padding)
								.padding(bottom = 90.dp, start = 16.dp, end = 16.dp, top = 4.dp),
							verticalArrangement = Arrangement.spacedBy(15.dp)
						) {
							items(profiles) { profile ->
								ProfileCard(
									profile = profile,
									isAutomaticMode = isAutomaticMode,
									onActivate = { viewModel.activateProfile(profile.id) },
									onDelete = {
										profileToDelete = profile.id
										showDialog = true
									},
									onEdit = { navController.navigate("${Route.EditProfile.destination}/${profile.id}") }
								)
							}
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

		// Konfirmasi Delete
		if (showDialog && profileToDelete != null) {
			AlertDialog(
				onDismissRequest = { showDialog = false },
				confirmButton = {
					TextButton(onClick = {
						profileToDelete?.let { id ->
							viewModel.deleteProfile(id)
							viewModel.loadProfiles() // Reload data setelah delete
						}
						showDialog = false
					}) {
						Text("Yes", color = MaterialTheme.colorScheme.primary)
					}
				},
				dismissButton = {
					TextButton(onClick = { showDialog = false }) {
						Text("No", color = MaterialTheme.colorScheme.error)
					}
				},
				title = { Text("Delete Profile") },
				text = { Text("Are you sure you want to delete this profile?") }
			)
		}
	}
}

@Composable
fun ProfileCard(
	profile: Profile,
	onActivate: () -> Unit,
	isAutomaticMode: Boolean,
	onDelete: () -> Unit,
	onEdit: () -> Unit
) {
	val textColor = MaterialTheme.colorScheme.onSurface
	val headColor = MaterialTheme.colorScheme.surface
	val backgroundColor = MaterialTheme.colorScheme.tertiaryContainer


	Card(
		modifier = Modifier
			.fillMaxWidth(),
		colors = CardDefaults.cardColors(
			containerColor = backgroundColor
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 2.dp
		)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = profile.profile,
					style = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.Bold
					),
					color = textColor
				)
				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					IconButton(onClick = onEdit) {
						Icon(
							Icons.Default.Edit,
							contentDescription = "Edit Profile",
							tint = Color.Gray
						)
					}
					IconButton(onClick = onDelete) {
						Icon(
							Icons.Default.Delete,
							contentDescription = "Delete Profile",
							tint = Color.Gray
						)
					}
				}
			}

			Spacer(modifier = Modifier.height(16.dp))

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				ProfileParameter(
					icon = Icons.Default.Thermostat,
					label = "Temperature",
					value = "${profile.watertemp}°C",
					color = Color(0xFF507CEA)
				)
				ProfileParameter(
					icon = Icons.Default.Water,
					label = "PPM",
					value = "${profile.waterppm}",
					color = Color(0xFFE6A23C)
				)
				ProfileParameter(
					icon = Icons.Default.PieChart,
					label = "pH",
					value = "${profile.waterph}",
					color = Color(0xFF9C27B0)
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			if (profile.status != 1) { // Jika tidak aktif
				Button(
					onClick = onActivate,
					modifier = Modifier.fillMaxWidth(),
					colors = ButtonDefaults.buttonColors(
						containerColor = if (isAutomaticMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
					),
					enabled = isAutomaticMode
				) {
					Text(
						text = "Activate",
						color = headColor,
						style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
						modifier = Modifier.fillMaxWidth(),
						textAlign = TextAlign.Center
					)
				}
			} else { // Jika aktif
				Button(
					onClick = onActivate,
					modifier = Modifier.fillMaxWidth(),
					colors = ButtonDefaults.buttonColors(
						containerColor = if (isAutomaticMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary
					),
					enabled = isAutomaticMode
				) {
					Text(
						text = "Active Profile",
						color = headColor,
						style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
						modifier = Modifier.fillMaxWidth(),
						textAlign = TextAlign.Center
					)
				}
			}

			if (!isAutomaticMode) {
				Text(
					text = "Profiles can only be activated in Automatic Mode",
					color = MaterialTheme.colorScheme.error,
					style = MaterialTheme.typography.bodySmall,
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					textAlign = TextAlign.Center
				)
			}

		}
	}
}

@Composable
fun ProfileParameter(icon: ImageVector, label: String, value: String, color: Color) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Box(
			modifier = Modifier
				.size(48.dp)
				.background(
					color = color.copy(alpha = 0.1f),
					shape = RoundedCornerShape(12.dp)
				),
			contentAlignment = Alignment.Center
		) {
			Icon(
				imageVector = icon,
				contentDescription = label,
				tint = color,
				modifier = Modifier.size(24.dp)
			)
		}
		Text(
			text = value,
			style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
		)
		Text(
			text = label,
			style = MaterialTheme.typography.bodySmall,
			color = Color.Gray
		)
	}
}

