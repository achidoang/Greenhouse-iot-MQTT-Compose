package com.kuliah.greenhouse_iot.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Search
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
				label = "Login",
				icon = Icons.Filled.Search,
				route = Route.Login.destination
			),
			BottomBarItem(
				label = "Profile",
				icon = Icons.Filled.MovieFilter,
				route = Route.Profile.destination
			)
		)
	}
}