package com.kuliah.greenhouse_iot.presentation.navigation

import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kuliah.greenhouse_iot.presentation.screen.actuator.ActuatorScreen
import com.kuliah.greenhouse_iot.presentation.screen.home.HomeScreen
import com.kuliah.greenhouse_iot.presentation.screen.login.LoginScreen
import com.kuliah.greenhouse_iot.presentation.screen.profile.ProfileScreen
import com.kuliah.greenhouse_iot.presentation.screen.setPoint.SetPointScreen
import com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring.MonitoringViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(UnstableApi::class)
@Composable
fun AppNavigationGraph(
	navHostController: NavHostController,
	modifier: Modifier,
//	darkTheme: Boolean,

) {
	NavHost(
		navController = navHostController,
		startDestination = Route.Home.destination
	) {

		composable(Route.Home.destination) {
			HomeScreen(
				modifier = Modifier,
				navController = navHostController,
				// Teruskan navController ke HomeScreen
				//				darkTheme = darkTheme, // Ubah sesuai kebutuhan Anda
			)
		}

		composable(Route.Profile.destination) {
			ProfileScreen()
		}

		composable(Route.Actuator.destination){
			ActuatorScreen()
		}

		composable(Route.Login.destination){
			LoginScreen()
		}

		composable(Route.SetPoint.destination){
			SetPointScreen()
		}

	}

}