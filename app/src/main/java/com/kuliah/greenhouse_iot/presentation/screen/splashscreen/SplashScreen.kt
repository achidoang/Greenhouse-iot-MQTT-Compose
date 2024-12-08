package com.kuliah.greenhouse_iot.presentation.screen.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kuliah.greenhouse_iot.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
	val splashScreenDelay = 1500L // Delay splash screen dalam milidetik
	val backgroundColor = MaterialTheme.colorScheme.background
	val logo = painterResource(id = R.drawable.splash_logo) // Ganti dengan logo Anda

	// Menggunakan LaunchedEffect untuk mengatur timeout
	LaunchedEffect(Unit) {
		delay(splashScreenDelay)
		onTimeout()
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(backgroundColor),
		contentAlignment = Alignment.Center
	) {
		Image(
			painter = logo,
			contentDescription = "Splash Logo",
			modifier = Modifier.size(90.dp) // Sesuaikan ukuran logo
		)
	}
}
