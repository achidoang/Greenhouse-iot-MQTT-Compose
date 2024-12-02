package com.kuliah.greenhouse_iot.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuliah.greenhouse_iot.presentation.screen.add_user.AddUserScreen
import com.kuliah.greenhouse_iot.presentation.screen.chart.AverageHistoryScreen
import com.kuliah.greenhouse_iot.presentation.screen.chart.ChartScreen
import com.kuliah.greenhouse_iot.presentation.screen.controll.ModeSelectionScreen
import com.kuliah.greenhouse_iot.presentation.screen.controll.manual.ActuatorScreen
import com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis.CreateProfileScreen
import com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis.EditProfileScreen
import com.kuliah.greenhouse_iot.presentation.screen.controll.otomatis.ProfileListScreen
import com.kuliah.greenhouse_iot.presentation.screen.edit_user.EditUserScreen
import com.kuliah.greenhouse_iot.presentation.screen.history.HistoryScreen
import com.kuliah.greenhouse_iot.presentation.screen.home.HomeScreen
import com.kuliah.greenhouse_iot.presentation.screen.login.LoginScreen
import com.kuliah.greenhouse_iot.presentation.screen.manage_user.ManageUserScreen
import com.kuliah.greenhouse_iot.presentation.screen.profile.UserProfileScreen
import com.kuliah.greenhouse_iot.presentation.screen.setPoint.SetPointScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(UnstableApi::class)
@Composable
fun AppNavigationGraph(
	navHostController: NavHostController,
	modifier: Modifier,
	isUserLoggedIn: Boolean,
	userRole: String
) {
	Log.d("AppNavigationGraph", "userRole: $userRole")
	NavHost(
		navController = navHostController,
		startDestination = if (isUserLoggedIn) Route.Home.destination else Route.Login.destination
	) {
		composable(Route.Home.destination) {
			HomeScreen(
				modifier = Modifier,
				navController = navHostController,
			)
		}

		composable(Route.Manage.destination) {
			Log.d("Navigation", "Navigated to Profile screen with role: $userRole")
			if (userRole == "user") {
				UserProfileScreen()
			} else {
				ManageUserScreen(
					onAddUser = { navHostController.navigate(Route.AddUser.destination) },
					onEditUser = { user ->
						navHostController.navigate("${Route.EditUser.destination}/${user.id}")
					},
					onLogout = {
						navHostController.navigate(Route.Login.destination) {
							popUpTo(Route.Manage.destination) { inclusive = true }
						}
					}
				)
			}
		}


		composable(Route.Actuator.destination) {
			ActuatorScreen()
		}

		composable(Route.Login.destination) {
			LoginScreen(
				onLoginSuccess = {
					// Navigasi ke layar utama setelah login
					navHostController.navigate(Route.Home.destination) {
						popUpTo(Route.Login.destination) { inclusive = true }
					}
				}
			)
		}

		composable(Route.SetPoint.destination) {
			SetPointScreen()
		}

		// Rute untuk AddUserScreen
		composable(Route.AddUser.destination) {
			AddUserScreen(
				onAddSuccess = {
					// Kembali ke halaman sebelumnya setelah user berhasil ditambahkan
					navHostController.popBackStack()
				}
			)
		}

		// Rute untuk EditUserScreen dengan parameter userId
		composable("${Route.EditUser.destination}/{id}") { backStackEntry ->
			val userId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
			EditUserScreen(
				userId = userId,
				onEditSuccess = {
					// Kembali setelah user berhasil diedit
					navHostController.popBackStack()
				}
			)
		}

		composable(Route.History.destination) {
			HistoryScreen()
		}

		composable(Route.Chart.destination) {
			ChartScreen()
		}

		composable(Route.Profile.destination) {
			ProfileListScreen(
				navController = navHostController,
			)
		}

		composable(Route.CreateProfile.destination) {
			CreateProfileScreen(
				onProfileCreated = { navHostController.navigateUp() },
				navController = navHostController
			)
		}

		composable("${Route.EditProfile.destination}/{id}") { backStackEntry ->
			val profileId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
			EditProfileScreen(
				profileId = profileId,
				onProfileUpdated = { navHostController.popBackStack() },
				navController = navHostController
			)
		}



		composable(Route.ControlMode.destination) {
			ModeSelectionScreen(
				navController = navHostController
			)
		}
	}
}
