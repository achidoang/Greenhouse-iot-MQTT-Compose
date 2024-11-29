package com.kuliah.greenhouse_iot.presentation.screen.controll.manual

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.controll.manual.ActuatorPayload
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.viewmodel.actuator.ControllAktuatorViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.actuator.UiState

@Composable
fun ActuatorScreen(viewModel: ControllAktuatorViewModel = hiltViewModel()) {
	val uiState by viewModel.uiState.collectAsState()
	val aktuatorState = viewModel.aktuatorState.collectAsState()

	val textColor = MaterialTheme.colorScheme.onSurface
	val backgroundColor = MaterialTheme.colorScheme.background

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(backgroundColor)
			.padding(16.dp)
	) {
		Text(
			text = "Manual Control",
			style = MaterialTheme.typography.titleLarge.copy(
				fontWeight = FontWeight.Bold
			),
			modifier = Modifier.padding(bottom = 16.dp),
			color = textColor
		)

		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			val payload = aktuatorState.value ?: ActuatorPayload(0, 0, 0, 0, 0, 0)

			listOf(
				Triple("Main Water Pump", payload.actuator_pompa_utama_1, Icons.Default.WaterDrop to 0xFF5B8DEF),
				Triple("Secondary Water Pump", payload.actuator_pompa_utama_2, Icons.Default.WaterDrop to 0xFFE6A23C),
				Triple("pH Increase Pump", payload.actuator_ph_up, Icons.Default.ArrowUpward to 0xFF9C27B0),
				Triple("pH Decrease Pump", payload.actuator_ph_down, Icons.Default.ArrowDownward to 0xFF5B8DEF),
				Triple("Nutrition Pump", payload.actuator_nutrisi, Icons.Default.MedicalServices to 0xFFE6A23C),
				Triple("Raw Water Pump", payload.actuator_air_baku, Icons.Default.Waves to 0xFF9C27B0)
			).forEach { (title, status, iconPair) ->
				item {
					ActuatorListItem(
						title = title,
						location = if (title.contains("Pump")) "Main System" else "Control System",
						icon = iconPair.first,
						iconTint = Color(iconPair.second),
						isOn = status == 1,
						onToggle = { isOn ->
							val updatedPayload = when (title) {
								"Main Water Pump" -> payload.copy(actuator_pompa_utama_1 = if (isOn) 1 else 0)
								"Secondary Water Pump" -> payload.copy(actuator_pompa_utama_2 = if (isOn) 1 else 0)
								"pH Increase Pump" -> payload.copy(actuator_ph_up = if (isOn) 1 else 0)
								"pH Decrease Pump" -> payload.copy(actuator_ph_down = if (isOn) 1 else 0)
								"Nutrition Pump" -> payload.copy(actuator_nutrisi = if (isOn) 1 else 0)
								"Raw Water Pump" -> payload.copy(actuator_air_baku = if (isOn) 1 else 0)
								else -> payload
							}
							viewModel.toggleActuatorStatus(updatedPayload)
						}
					)
				}
			}
		}

		if (uiState != UiState.Idle) {
			Box(
				modifier = Modifier.fillMaxWidth(),
				contentAlignment = Alignment.Center
			) {
				when (uiState) {
					is UiState.Loading -> StatusChip("Updating...", MaterialTheme.colorScheme.primary)
					is UiState.Success -> StatusChip("Update successful!", MaterialTheme.colorScheme.secondary)
					is UiState.Error -> StatusChip("Update failed!", MaterialTheme.colorScheme.error)
					else -> {}
				}
			}
		}
	}
}

@Composable
fun ActuatorListItem(
	title: String,
	location: String,
	icon: ImageVector,
	iconTint: Color,
	isOn: Boolean,
	onToggle: (Boolean) -> Unit
) {
	val textColor = MaterialTheme.colorScheme.onSurface
	val headColor = MaterialTheme.colorScheme.onBackground
	val backgroundColor = MaterialTheme.colorScheme.tertiaryContainer

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.clickable { onToggle(!isOn) },
		colors = CardDefaults.cardColors(containerColor = backgroundColor),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 2.dp
		)
	) {
		Row(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Box(
					modifier = Modifier
						.size(48.dp)
						.background(
							color = iconTint.copy(alpha = 0.1f),
							shape = RoundedCornerShape(12.dp)
						),
					contentAlignment = Alignment.Center
				) {
					Icon(
						imageVector = icon,
						contentDescription = null,
						tint = iconTint,
						modifier = Modifier.size(24.dp)
					)
				}

				Column {
					Text(
						text = title,
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.Medium
						),
						color = headColor
					)
					Text(
						text = location,
						style = MaterialTheme.typography.bodyMedium,
						color = textColor
					)
				}
			}

			Text(
				text = if (isOn) "On" else "Off",
				style = MaterialTheme.typography.bodyMedium,
				color = if (isOn) textColor else MaterialTheme.colorScheme.primary
			)
		}
	}
}

@Composable
fun StatusChip(status: String, backgroundColor: Color) {
	Surface(
		color = backgroundColor.copy(alpha = 0.1f),
		shape = RoundedCornerShape(16.dp),
		modifier = Modifier.padding(8.dp)
	) {
		Text(
			text = status,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
			color = backgroundColor,
			style = MaterialTheme.typography.bodyMedium
		)
	}
}



//@Composable
//fun ActuatorScreen(viewModel: ControllAktuatorViewModel = hiltViewModel()) {
//	val uiState by viewModel.uiState.collectAsState()
//	val aktuatorState = viewModel.aktuatorState.collectAsState()
//
//	Column(
//		modifier = Modifier.fillMaxSize(),
//		horizontalAlignment = Alignment.CenterHorizontally,
//		verticalArrangement = Arrangement.Center
//	) {
//		Spacer(Modifier.height(14.dp))
//		Text(
//			text = "(Kontrol Manual)",
//			style = MaterialTheme.typography.titleMedium
//		)
//		// Add your manual control UI components here
//		Box(modifier = Modifier.fillMaxSize()) {
//			Column(
//				modifier = Modifier
//					.fillMaxWidth()
//					.padding(16.dp),
//				horizontalAlignment = Alignment.CenterHorizontally
//			) {
//				// LazyVerticalGrid untuk tombol
//				LazyVerticalGrid(
//					columns = GridCells.Fixed(3),
//					modifier = Modifier
//						.fillMaxWidth()
//						.padding(8.dp),
//					verticalArrangement = Arrangement.spacedBy(16.dp),
//					horizontalArrangement = Arrangement.spacedBy(16.dp)
//				) {
//					val payload = aktuatorState.value ?: ActuatorPayload(0, 0, 0, 0, 0, 0)
//
//					// Water Drain Pump
//					item {
//						ActuatorButton(
//							title = "Water Drain Pump",
//							icon = Icons.Default.WaterDrop,
//							isOn = payload.actuator_pompa_utama_1 == 1,
//							onToggle = { isOn ->
//								viewModel.toggleActuatorStatus(
//									payload.copy(actuator_pompa_utama_1 = if (isOn) 1 else 0)
//								)
//							}
//						)
//					}
//					// Water Filling Pump
//					item {
//						ActuatorButton(
//							title = "Water Filling Pump",
//							icon = Icons.Default.WaterDrop,
//							isOn = payload.actuator_pompa_utama_2 == 1,
//							onToggle = { isOn ->
//								viewModel.toggleActuatorStatus(
//									payload.copy(actuator_pompa_utama_2 = if (isOn) 1 else 0)
//								)
//							}
//						)
//					}
//					// pH Liquid Increase
//					item {
//						ActuatorButton(
//							title = "pH Liquid ↑",
//							icon = Icons.Default.ArrowUpward,
//							isOn = payload.actuator_ph_up == 1,
//							onToggle = { isOn ->
//								viewModel.toggleActuatorStatus(
//									payload.copy(actuator_ph_up = if (isOn) 1 else 0)
//								)
//							}
//						)
//					}
//					// pH Liquid Decrease
//					item {
//						ActuatorButton(
//							title = "pH Liquid ↓",
//							icon = Icons.Default.ArrowDownward,
//							isOn = payload.actuator_ph_down == 1,
//							onToggle = { isOn ->
//								viewModel.toggleActuatorStatus(
//									payload.copy(actuator_ph_down = if (isOn) 1 else 0)
//								)
//							}
//						)
//					}
//					// Nutrisi
//					item {
//						ActuatorButton(
//							title = "Nutrisi",
//							icon = Icons.Default.MedicalServices,
//							isOn = payload.actuator_nutrisi == 1,
//							onToggle = { isOn ->
//								viewModel.toggleActuatorStatus(
//									payload.copy(actuator_nutrisi = if (isOn) 1 else 0)
//								)
//							}
//						)
//					}
//					// Air Baku
//					item {
//						ActuatorButton(
//							title = "Air Baku",
//							icon = Icons.Default.Waves,
//							isOn = payload.actuator_air_baku == 1,
//							onToggle = { isOn ->
//								viewModel.toggleActuatorStatus(
//									payload.copy(actuator_air_baku = if (isOn) 1 else 0)
//								)
//							}
//						)
//					}
//				}
//			}
//
//			// Display status based on uiState
//			if (uiState != UiState.Idle) {
//				Box(
//					modifier = Modifier.fillMaxSize(),
//					contentAlignment = Alignment.Center
//				) {
//					when (uiState) {
//						is UiState.Loading -> FloatingConnectionStatus(
//							"Updating...",
//							MaterialTheme.colorScheme.primary
//						)
//
//						is UiState.Success -> FloatingConnectionStatus(
//							"Update successful!",
//							MaterialTheme.colorScheme.secondary
//						)
//
//						is UiState.Error -> FloatingConnectionStatus(
//							"Update failed!",
//							MaterialTheme.colorScheme.error
//						)
//
//						else -> {}
//					}
//				}
//			}
//
//		}
//	}
//
//}
//
//@Composable
//fun ActuatorButton(
//	title: String,
//	icon: ImageVector,
//	isOn: Boolean,
//	onToggle: (Boolean) -> Unit
//) {
//	ElevatedCard(
//		modifier = Modifier
//			.size(100.dp), // Ukuran tetap
//		colors = CardDefaults.elevatedCardColors(
//			containerColor = if (isOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
//			contentColor = MaterialTheme.colorScheme.onPrimary
//		),
//		onClick = { onToggle(!isOn) }
//	) {
//		Column(
//			modifier = Modifier
//				.fillMaxSize()
//				.padding(8.dp),
//			horizontalAlignment = Alignment.CenterHorizontally,
//			verticalArrangement = Arrangement.Center
//		) {
//			Icon(
//				imageVector = icon,
//				contentDescription = null,
//				tint = if (isOn) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
//				modifier = Modifier.size(32.dp)
//			)
//			Spacer(modifier = Modifier.height(8.dp))
//			Text(
//				text = title,
//				color = if (isOn) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
//				style = MaterialTheme.typography.bodyMedium,
//				textAlign = TextAlign.Center
//			)
//		}
//	}
//}
//
//@Composable
//fun FloatingConnectionStatus(status: String, backgroundColor: Color) {
//	Box(
//		modifier = Modifier
//			.clip(RoundedCornerShape(16.dp))
//			.background(backgroundColor)
//			.padding(8.dp)
//	) {
//		Text(
//			text = status,
//			color = MaterialTheme.colorScheme.onPrimary,
//			style = MaterialTheme.typography.bodySmall,
//			textAlign = TextAlign.Center
//		)
//	}
//}



