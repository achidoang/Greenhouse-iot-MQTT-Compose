package com.kuliah.greenhouse_iot.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuliah.greenhouse_iot.presentation.navigation.BottomBarItem
import com.kuliah.greenhouse_iot.presentation.navigation.Route

@Composable
fun AppBottomBar(navController: NavHostController) {
	val items = BottomBarItem().getBottomNavigationItems()

	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination

	if (items.any { it.route == currentDestination?.route }) {
		NavigationBar(
			modifier = Modifier
				.fillMaxWidth()
				.padding(12.dp)
				.height(64.dp)// Padding untuk jarak dari tepi layar
				.clip(RoundedCornerShape(25.dp)), // Membuat sudut melengkung
			containerColor = Color(0xFF2D2D2D), // Warna latar belakang Navigation Bar
		) {
			items.forEach { item ->
				val isSelected = currentDestination?.route == item.route

				NavigationBarItem(
					selected = isSelected,
					icon = {
						Icon(
							imageVector = item.icon,
							contentDescription = item.label,
							tint = if (isSelected) Color(0xFF03DAC5) // Warna orange untuk ikon aktif
							else Color.White.copy(alpha = 0.4f) // Warna putih transparan untuk ikon tidak aktif
						)
					},
					label = {
						Text(
							text = item.label,
							style = MaterialTheme.typography.labelSmall,
							color = if (isSelected) Color(0xFF03DAC5) // Warna teks untuk item aktif
							else Color.White.copy(alpha = 0.4f) // Warna teks untuk item tidak aktif
						)
					},
					onClick = {
						navController.navigate(item.route) {
							popUpTo(navController.graph.findStartDestination().id) {
								saveState = true
							}
							launchSingleTop = true
							restoreState = true
						}
					},
					alwaysShowLabel = false // Menyembunyikan label saat tidak aktif
				)
			}
		}
	}
}
