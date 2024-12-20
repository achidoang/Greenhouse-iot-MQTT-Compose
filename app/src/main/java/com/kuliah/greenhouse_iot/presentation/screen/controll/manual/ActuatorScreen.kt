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
import androidx.compose.runtime.LaunchedEffect
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
import com.kuliah.greenhouse_iot.presentation.viewmodel.mode.ModeViewModel
import kotlinx.coroutines.delay

@Composable
fun ActuatorScreen(
	viewModel: ControllAktuatorViewModel = hiltViewModel(),
	modeViewModel: ModeViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsState()
	val aktuatorState = viewModel.aktuatorState.collectAsState()

	val textColor = MaterialTheme.colorScheme.onSurface
	val backgroundColor = MaterialTheme.colorScheme.background

	// Mengamati nilai dari DataStore
	val isAutomaticMode by modeViewModel.isAutomaticMode.collectAsState(initial = true)


	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(backgroundColor)
			.padding(16.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 16.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = if (isAutomaticMode) "Mode: Otomatis" else "Mode: Manual",
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Bold
			)
			Switch(
				checked = isAutomaticMode,
				onCheckedChange = { isChecked ->
					modeViewModel.setAutomaticMode(isChecked)
				}
			)
		}

		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			val payload = aktuatorState.value ?: ActuatorPayload(0, 0, 0, 0, 0, 0)

			listOf(
				Triple(
					"Main Water Pump",
					payload.actuator_pompa_utama_1,
					Icons.Default.WaterDrop to 0xFF5B8DEF
				),
				Triple(
					"Secondary Water Pump",
					payload.actuator_pompa_utama_2,
					Icons.Default.WaterDrop to 0xFFE6A23C
				),
				Triple(
					"pH Increase Pump",
					payload.actuator_ph_up,
					Icons.Default.ArrowUpward to 0xFF9C27B0
				),
				Triple(
					"pH Decrease Pump",
					payload.actuator_ph_down,
					Icons.Default.ArrowDownward to 0xFF5B8DEF
				),
				Triple(
					"Nutrition Pump",
					payload.actuator_nutrisi,
					Icons.Default.MedicalServices to 0xFFE6A23C
				),
				Triple(
					"Raw Water Pump",
					payload.actuator_air_baku,
					Icons.Default.Waves to 0xFF9C27B0
				)
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
						},
						isEnabled = !isAutomaticMode // Aktuator hanya aktif jika mode manual
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
					is UiState.Loading -> StatusChip(
						"Updating...",
						MaterialTheme.colorScheme.primary
					)

					is UiState.Success -> StatusChip(
						"Update successful!",
						MaterialTheme.colorScheme.secondary
					)

					is UiState.Error -> StatusChip(
						"Update failed!",
						MaterialTheme.colorScheme.error
					)

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
	onToggle: (Boolean) -> Unit,
	isEnabled: Boolean
) {
	val textColor = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
	val headColor = if (isEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
	val backgroundColor = if (isEnabled) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.clickable(enabled = isEnabled) { onToggle(!isOn) },
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
							color = iconTint.copy(alpha = if (isEnabled) 0.1f else 0.05f),
							shape = RoundedCornerShape(12.dp)
						),
					contentAlignment = Alignment.Center
				) {
					Icon(
						imageVector = icon,
						contentDescription = null,
						tint = if (isEnabled) iconTint else iconTint.copy(alpha = 0.4f),
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
				color = if (isOn) textColor else MaterialTheme.colorScheme.primary.copy(alpha = if (isEnabled) 1f else 0.4f),
				fontWeight = if (isOn) FontWeight.Bold else FontWeight.Normal
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




