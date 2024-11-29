package com.kuliah.greenhouse_iot

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kuliah.greenhouse_iot.data.local.datastore.AuthDataStoreManager
import com.kuliah.greenhouse_iot.presentation.common.AppBottomBar
import com.kuliah.greenhouse_iot.presentation.navigation.AppNavigationGraph
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.auth.AuthViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring.MonitoringViewModel
import com.kuliah.greenhouse_iot.ui.theme.GreenhouseiotTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@Inject
	lateinit var authDataStoreManager: AuthDataStoreManager



	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen()


		setContent {
//			val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
//			val themeMode = homeScreenViewModel.themeMode.collectAsState().value

			val backgroundColor = MaterialTheme.colorScheme.background // Warna latar Navigation Bar


			GreenhouseiotTheme(
				darkTheme = isSystemInDarkTheme(),
				dynamicColor = false
			) {
				// Create a SystemUiController instance
				val systemUiController = rememberSystemUiController()
//				val useDarkIcons = !themeMode

				// Set the status bar color
				SideEffect {
					systemUiController.setStatusBarColor(
						color = backgroundColor,
						darkIcons = true // Atur true jika teks status bar harus gelap
					)
				}


				Surface(
					modifier = Modifier
						.fillMaxSize()
						.navigationBarsPadding()
						.statusBarsPadding(),
					color = MaterialTheme.colorScheme.background
				) {
					MainScreen(authDataStoreManager)
				}
			}
		}
	}
	@SuppressLint("NewApi")
	@Composable
	fun MainScreen(
		authDataStoreManager: AuthDataStoreManager,
//		darkTheme: Boolean,
	) {

		val navController = rememberNavController()
		val authViewModel: AuthViewModel = hiltViewModel()
		val monitoringViewModel: MonitoringViewModel = hiltViewModel()
		val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()

		// Mulai layanan di dalam LaunchedEffect
		// realita kode ini ketika ada data maka dinyatakan device connected, namun ketika tidak ada data tetap device connected, tapi ketika tidak ada jaringan internet baru dinyatakan device offline

		LaunchedEffect(Unit) {
			monitoringViewModel.startMonitoringService()
		}

		LaunchedEffect(isUserLoggedIn) {
			if (!isUserLoggedIn) {
				navController.navigate(Route.Login.destination) {
					popUpTo(Route.Home.destination) { inclusive = true }
				}
			}
		}

		Scaffold(
			bottomBar = { AppBottomBar(navController = navController) }
		) {
			val userRole by authDataStoreManager.getUserRole()
				.map { it ?: "user" }
				.collectAsState(initial = "user")
			AppNavigationGraph(
				navHostController = navController,
				modifier = Modifier.padding(it),
				isUserLoggedIn = isUserLoggedIn,
				userRole = userRole
			)
		}
	}
}


