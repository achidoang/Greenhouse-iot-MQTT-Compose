package com.kuliah.greenhouse_iot.presentation.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.presentation.common.GradientBox
import com.kuliah.greenhouse_iot.presentation.common.MyTextField
import com.kuliah.greenhouse_iot.presentation.viewmodel.login.LoginState
import com.kuliah.greenhouse_iot.presentation.viewmodel.login.LoginViewModel
import com.kuliah.greenhouse_iot.util.rememberImeState

@Composable
fun LoginScreen(
	viewModel: LoginViewModel = hiltViewModel(),
	onLoginSuccess: () -> Unit
) {
	val isImeVisible by rememberImeState()
	val loginState by viewModel.loginState.collectAsState()

	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	val scaffoldState = rememberScaffoldState()

	Scaffold(
		scaffoldState = scaffoldState,
		snackbarHost = {
			SnackbarHost(
				hostState = it,
				modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.Top),
				snackbar = { data ->
					Snackbar(
						snackbarData = data,
						backgroundColor = Color(0xFFEB5757),
						contentColor = Color.White,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
				}
			)
		}
	) { padding ->
		GradientBox(modifier = Modifier.fillMaxSize()) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(padding),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				val animatedUpperSectionRatio by animateFloatAsState(
					targetValue = if (isImeVisible) 0f else 0.35f,
					label = "",
				)
				AnimatedVisibility(visible = !isImeVisible, enter = fadeIn(), exit = fadeOut()) {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.fillMaxHeight(animatedUpperSectionRatio),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = "Welcome to AutoGreen",
							style = MaterialTheme.typography.headlineMedium,
							color = Color.White
						)
					}
				}
				Column(
					modifier = Modifier
						.fillMaxSize()
						.clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
						.background(Color.White),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Spacer(modifier = Modifier.height(16.dp))
					Text(
						text = "Log in",
						style = MaterialTheme.typography.headlineMedium,
						color = Color.Black
					)
					Spacer(modifier = Modifier.height(24.dp))

					MyTextField(
						modifier = Modifier.padding(horizontal = 16.dp),
						value = username,
						onValueChange = { username = it },
						label = "Username",
						keyboardOptions = KeyboardOptions.Default,
						keyboardActions = KeyboardActions.Default
					)
					Spacer(modifier = Modifier.height(20.dp))
					MyTextField(
						modifier = Modifier.padding(horizontal = 16.dp),
						value = password,
						onValueChange = { password = it },
						label = "Password",
						keyboardOptions = KeyboardOptions.Default,
						keyboardActions = KeyboardActions.Default,
						trailingIcon = Icons.Default.Lock
					)
					Spacer(modifier = Modifier.height(20.dp))
					Button(
						onClick = { viewModel.login(username, password) },
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF03DAC5),
							contentColor = Color.White
						),
						shape = RoundedCornerShape(10.dp)
					) {
						if (loginState is LoginState.Loading) {
							CircularProgressIndicator(
								color = Color.White,
								modifier = Modifier.size(24.dp)
							)
						} else {
							Text(
								text = "Log in",
								style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
							)
						}
					}
				}
			}
		}

		// Tangani status login
		when (loginState) {
			is LoginState.Success -> {
				LaunchedEffect(Unit) {
					onLoginSuccess()
				}
			}
			is LoginState.Error -> {
				val errorMessage = when ((loginState as LoginState.Error).message) {
					"HTTP 400" -> "Username atau password salah."
					"No Internet" -> "Tidak ada koneksi internet. Harap periksa jaringan Anda."
					"Unknown Error" -> "Terjadi kesalahan yang tidak diketahui."
					else -> "Kesalahan: ${(loginState as LoginState.Error).message}"
				}
				LaunchedEffect(scaffoldState.snackbarHostState) {
					scaffoldState.snackbarHostState.showSnackbar(errorMessage)
				}
			}
			else -> {}
		}
	}
}

