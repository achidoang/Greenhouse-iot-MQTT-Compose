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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kuliah.greenhouse_iot.data.local.datastore.AuthDataStoreManager
import com.kuliah.greenhouse_iot.presentation.common.AppBottomBar
import com.kuliah.greenhouse_iot.presentation.navigation.AppNavigationGraph
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.screen.splashscreen.SplashScreen
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

		setContent {
			GreenhouseiotTheme(
				darkTheme = isSystemInDarkTheme(),
				dynamicColor = false
			) {
				// Mengontrol status bar dengan Material 3
				val systemUiController = rememberSystemUiController()
				val backgroundColor = MaterialTheme.colorScheme.background
				val useDarkIcons = !isSystemInDarkTheme()

				SideEffect {
					systemUiController.setStatusBarColor(
						color = backgroundColor,
						darkIcons = useDarkIcons
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
	) {
		val navController = rememberNavController()
		val authViewModel: AuthViewModel = hiltViewModel()
		val monitoringViewModel: MonitoringViewModel = hiltViewModel()
		val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()
		var showSplash by remember { mutableStateOf(true) } // Untuk mengontrol tampilan SplashScreen

		if (showSplash) {
			// Menampilkan SplashScreen terlebih dahulu
			SplashScreen {
				showSplash = false // Beralih ke UI utama setelah splash selesai
			}
		} else {
			// Logika utama aplikasi
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
				bottomBar = {
					val currentRoute =
						navController.currentBackStackEntryAsState().value?.destination?.route
					if (currentRoute != Route.Login.destination) {
						AppBottomBar(navController = navController)
					}
				}
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

}

