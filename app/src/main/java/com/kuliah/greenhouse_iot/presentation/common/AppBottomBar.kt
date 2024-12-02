package com.kuliah.greenhouse_iot.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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

	val backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
	val iconColor = MaterialTheme.colorScheme.primary

	// Menentukan rute yang ingin menampilkan FAB (semua rute dari BottomBarItem)
	val showFab = items.any { it.route == currentDestination?.route }
	//	val showFab = currentDestination?.route == Route.Home.destination
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 4.dp),
		contentAlignment = Alignment.BottomCenter
	) {
		// Main Navigation Bar
		if (showFab) {
			NavigationBar(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 24.dp, vertical = 16.dp)
					.height(64.dp)
					.clip(RoundedCornerShape(32.dp))
					.shadow(elevation = 4.dp, shape = RoundedCornerShape(32.dp)),
				containerColor = backgroundColor
			) {
				items.forEachIndexed { index, item ->
					val isSelected = currentDestination?.route == item.route
					val isMiddleItem = index == 2 // Center item

					if (!isMiddleItem) {
						NavigationBarItem(
							selected = isSelected,
							onClick = {
								navController.navigate(item.route) {
									popUpTo(navController.graph.findStartDestination().id) {
										saveState = true
									}
									launchSingleTop = true
									restoreState = true
								}
							},
							icon = {
								Column(
									horizontalAlignment = Alignment.CenterHorizontally
								) {
									Icon(
										imageVector = item.icon,
										contentDescription = item.label,
										tint = if (isSelected) iconColor else Color.Gray,
										modifier = Modifier.size(24.dp)
									)
									if (isSelected) {
										Box(
											modifier = Modifier
												.padding(top = 4.dp)
												.size(4.dp)
												.background(
													color = iconColor,
													shape = CircleShape
												)
										)
									}
								}
							},
							colors = NavigationBarItemDefaults.colors(
								selectedIconColor = iconColor,
								unselectedIconColor = Color.Gray,
								indicatorColor = Color.Transparent
							)
						)
					} else {
						// Spacer for middle item
						NavigationBarItem(
							selected = false,
							onClick = { },
							icon = { Box(modifier = Modifier.width(72.dp)) },
							colors = NavigationBarItemDefaults.colors(
								indicatorColor = Color.Transparent
							)
						)
					}
				}
			}

		}

		Box(
			modifier = Modifier
				.offset(y = (-40).dp) // Menggeser ke atas
				.size(55.dp + 15.dp) // Ukuran FAB + border
				.background(MaterialTheme.colorScheme.background, CircleShape) // Warna border
				.clip(CircleShape) // Membuat bentuk lingkaran
		) {
			FloatingActionButton(
				onClick = {
					navController.navigate(Route.Home.destination) {
						popUpTo(navController.graph.findStartDestination().id) {
							saveState = true
						}
						launchSingleTop = true
						restoreState = true
					}
				},
				modifier = Modifier
					.align(Alignment.Center) // Memposisikan FAB di tengah
					.size(55.dp), // Ukuran FAB (lebih kecil dari border)
				containerColor = iconColor,
				shape = CircleShape
			) {
				Icon(
					imageVector = items[2].icon,
					contentDescription = items[2].label,
					tint = Color.White
				)
			}
		}
	}
}

