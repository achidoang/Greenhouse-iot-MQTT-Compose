package com.kuliah.greenhouse_iot.presentation.screen.setPoint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SetPointScreen(
) {

	var isEditing by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Text(
			text = "Setpoints",
			style = MaterialTheme.typography.titleLarge,
			modifier = Modifier.padding(bottom = 16.dp)
		)



		Spacer(modifier = Modifier.height(16.dp))

		// Edit/Confirm/Cancel Buttons
		Row(
			horizontalArrangement = Arrangement.End,
			modifier = Modifier.fillMaxWidth()
		) {
			if (isEditing) {
				Button(
					onClick = {
						isEditing = false
					}
				) {
					Text(text = "Confirm")
				}

				Spacer(modifier = Modifier.width(8.dp))

				Button(
					onClick = {
						isEditing = false
					}
				) {
					Text(text = "Cancel")
				}
			} else {
				Button(
					onClick = {
						isEditing = true
					}
				) {
					Text(text = "Edit")
				}
			}
		}
	}
}

@Composable
fun SetPointSliderAndInput(
	label: String,
	value: Float,
	range: ClosedFloatingPointRange<Float>,
	isEnabled: Boolean,
	onValueChange: (Float) -> Unit
) {
	var textValue by remember { mutableStateOf(value.toString()) }

	// Update textValue whenever value changes
	LaunchedEffect(value) {
		textValue = value.toString()
	}

	Column(modifier = Modifier.fillMaxWidth()) {
		Text(text = label, style = MaterialTheme.typography.bodyLarge)

		Row(verticalAlignment = Alignment.CenterVertically) {
			Slider(
				value = value,
				onValueChange = {
					textValue = it.toString()
					onValueChange(it)
				},
				valueRange = range,
				enabled = isEnabled,
				modifier = Modifier.weight(1f)
			)

			Spacer(modifier = Modifier.width(16.dp))

			TextField(
				value = textValue,
				onValueChange = { newValue ->
					val floatVal = newValue.toFloatOrNull()
					if (floatVal != null && floatVal in range) {
						textValue = newValue
						onValueChange(floatVal)
					}
				},
				enabled = isEnabled,
				modifier = Modifier.width(80.dp),
				singleLine = true
			)
		}

		Spacer(modifier = Modifier.height(8.dp))
	}
}
