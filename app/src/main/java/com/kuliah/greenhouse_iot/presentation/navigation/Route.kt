package com.kuliah.greenhouse_iot.presentation.navigation

sealed class Route(val destination: String) {
	data object Home : Route("home")
	data object Profile : Route("profile")
	data object Login : Route("login")
	data object Actuator : Route("actuator")
	data object SetPoint : Route("setPoint")
	data object AddUser : Route("add_user")
	data object EditUser : Route("edit_user")
}