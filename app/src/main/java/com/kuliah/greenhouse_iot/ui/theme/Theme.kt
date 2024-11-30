package com.kuliah.greenhouse_iot.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
	primary = Color(0xFF03DAC5),
	secondary = Color(0xFF8BC34A), // Hijau terang
	tertiary = Color(0xFF00857B), // Oranye pekat
	surface = Color(0xFF1B1F23), // Hitam kehijauan
	onSurface = Color(0xFFFFFFFF), // Putih terang
	background = Color(0xFF121212), // Hitam netral
	tertiaryContainer = Color(0xFF263238), // Abu-abu kebiruan
	onBackground = Color(0xFFA5D6A7) // Hijau pastel terang
)

private val LightColorScheme = lightColorScheme(
	primary = Color(0xFF03DAC5),
	secondary = Color(0xFF81D4FA), // Biru muda lembut
	tertiary = Color(0xFFA5D6A7), // Hijau pastel pucat
	background = Color(0xFFF0F7FA), // Putih kebiruan
	onSurface = Color(0xFF202B2F), // Abu-abu kebiruan gelap
	surface = Color(0xFFF3F3FA), // Biru sangat pucat
	tertiaryContainer = Color(0xFFB2EBF2), // Biru aqua pastel
	onBackground = Color(0xFF37474F) // Abu-abu kebiruan
)




//private val DarkColorScheme = darkColorScheme(
//	primary = Color(0xFF03DAC5),
//	secondary = PurpleGrey80,
//	tertiary = Pink80,
//	surface = Color(0xFF1F2633),
//	onSurface = Color(0xFFFFFFFF),
//	background = Color(0xFF00110f),
//	onBackground = Color(0xFF79fdf0),
//	tertiaryContainer = Color(0xFF01433d)
//
//)
//
//private val LightColorScheme = lightColorScheme(
//	primary = Color(0xFF03DAC5),
//	secondary = PurpleGrey40,
//	tertiary = Pink40,
//	surface = Color(0xFFEAEBEF),
//	onSurface = Color(0xFF000000),
//	background = Color(0xFFddfffb),
//	onBackground = Color(0xFF003e40),
//	tertiaryContainer = Color(0xFF79fdf0)





	@Composable
fun GreenhouseiotTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// Dynamic color is available on Android 12+
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}

		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}