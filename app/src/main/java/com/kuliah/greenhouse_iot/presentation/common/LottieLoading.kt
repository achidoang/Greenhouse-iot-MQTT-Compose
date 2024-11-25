package com.kuliah.greenhouse_iot.presentation.common

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kuliah.greenhouse_iot.R

@Composable
fun LottieLoading() {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
	val progress by animateLottieCompositionAsState(composition)

	LottieAnimation(
		composition = composition,
		progress = progress,
		modifier = Modifier.size(150.dp)
	)
}
