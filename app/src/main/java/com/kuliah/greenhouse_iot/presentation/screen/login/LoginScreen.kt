package com.kuliah.greenhouse_iot.presentation.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen() {
	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	Column {
		TextField(
			value = username,
			onValueChange = { username = it },
			label = { Text("Username") }
		)

		TextField(
			value = password,
			onValueChange = { password = it },
			label = { Text("Password") },
			visualTransformation = PasswordVisualTransformation()
		)

	}
}
