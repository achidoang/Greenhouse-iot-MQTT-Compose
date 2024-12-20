package com.kuliah.greenhouse_iot.presentation.screen.controll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.screen.controll.manual.ActuatorScreen
import com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis.ProfileListScreen
import com.kuliah.greenhouse_iot.presentation.viewmodel.mode.ModeViewModel

@Composable
fun ModeSelectionScreen(
	navController: NavController,
) {
	val tabs = listOf("Aktuator", "Profil Setpoint")
	var selectedTabIndex by remember { mutableStateOf(0) }



	Column(modifier = Modifier.fillMaxSize()) {
		TabRow(
			selectedTabIndex = selectedTabIndex,
			containerColor = MaterialTheme.colorScheme.background,
			contentColor = MaterialTheme.colorScheme.onBackground,
			indicator = { tabPositions ->
				TabRowDefaults.Indicator(
					Modifier
						.tabIndicatorOffset(tabPositions[selectedTabIndex])
						.height(5.dp)
						.background(MaterialTheme.colorScheme.secondary)
				)
			}
		) {
			tabs.forEachIndexed { index, title ->
				Tab(
					selected = selectedTabIndex == index,
					onClick = { selectedTabIndex = index },
					text = { Text(title) }
				)
			}
		}

		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(0.dp, 0.dp, 0.dp, 4.dp)
		) {
			when (selectedTabIndex) {
				0 -> ActuatorScreen() // Kontrol Manual
				1 -> ProfileListScreen(navController = navController) // Kontrol Otomatis
			}
		}
	}
}
