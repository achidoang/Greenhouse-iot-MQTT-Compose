package com.kuliah.greenhouse_iot.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.login.LoginState
import com.kuliah.greenhouse_iot.presentation.viewmodel.login.LoginViewModel

@Composable
fun LoginScreen(
	viewModel: LoginViewModel = hiltViewModel(),
	onLoginSuccess: () -> Unit,
) {
	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	val loginState by viewModel.loginState.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		TextField(
			value = username,
			onValueChange = { username = it },
			label = { Text("Username") },
			modifier = Modifier.fillMaxWidth()
		)
		Spacer(modifier = Modifier.height(8.dp))
		TextField(
			value = password,
			onValueChange = { password = it },
			label = { Text("Password") },
			modifier = Modifier.fillMaxWidth()
		)
		Spacer(modifier = Modifier.height(16.dp))
		Button(
			onClick = { viewModel.login(username, password) },
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Login")
		}

		Spacer(modifier = Modifier.height(8.dp))

		when (loginState) {
			is LoginState.Loading -> Text("Loading...")
			is LoginState.Success -> {
				onLoginSuccess()
			}
			is LoginState.Error -> Text("Error: ${(loginState as LoginState.Error).message}")
			else -> {}
		}
	}
}