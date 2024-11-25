package com.kuliah.greenhouse_iot.presentation.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedLoading() {
	val infiniteTransition = rememberInfiniteTransition()
	val alpha by infiniteTransition.animateFloat(
		initialValue = 0.3f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(durationMillis = 1000, easing = LinearEasing),
			repeatMode = RepeatMode.Reverse
		)
	)

	Text(
		text = "Loading...",
		style = MaterialTheme.typography.headlineSmall,
		modifier = Modifier
			.alpha(alpha)
			.padding(16.dp),
		color = MaterialTheme.colorScheme.primary
	)
}
