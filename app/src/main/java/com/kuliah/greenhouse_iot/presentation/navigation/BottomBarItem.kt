package com.kuliah.greenhouse_iot.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplaneTicket
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material.icons.filled.Tv
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomBarItem(
	val label : String = "",
	val icon : ImageVector = Icons.Filled.Home,
	val route : String = "",
) {

	fun getBottomNavigationItems() : List<BottomBarItem> {
		return listOf(
			BottomBarItem(
				label = "Home",
				icon = Icons.Filled.Home,
				route = Route.Home.destination
			),
			BottomBarItem(
				label = "Grafik",
				icon = Icons.Filled.GraphicEq,
				route = Route.Chart.destination
			),
			BottomBarItem(
				label = "Profile",
				icon = Icons.Filled.DisplaySettings,
				route = Route.Profile.destination
			),
			BottomBarItem(
				label = "History",
				icon = Icons.Filled.History,
				route = Route.History.destination
			)
		)
	}
}