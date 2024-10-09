package com.kuliah.greenhouse_iot.presentation.navigation

sealed class Route( val destination: String ) {
	data object Home: Route("home")
	data object Profile: Route("profile")
	data object Pager: Route("pager")
	data object Login: Route("login")

}