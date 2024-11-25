package com.kuliah.greenhouse_iot.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NoInternetComponent(
	modifier: Modifier,
	error: String,
) {
	Column(
		modifier = modifier.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Icon(
			modifier = Modifier
				.size(64.dp),
			imageVector = Icons.Rounded.Warning, contentDescription = null
		)
		Text(
			modifier = Modifier
				.padding(8.dp),
			text = error,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.bodyLarge
		)

	}
}

@Preview(showBackground = true)
@Composable
fun NoInternetComponentPreview() {
	// Panggil NoInternetComponent dengan parameter yang sesuai untuk preview
	NoInternetComponent(
		modifier = Modifier.fillMaxSize(),
		error = "No internet connection",
	)
}