package com.kuliah.greenhouse_iot.presentation.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.R
import com.kuliah.greenhouse_iot.presentation.common.GradientBox
import com.kuliah.greenhouse_iot.presentation.common.MyTextField
import com.kuliah.greenhouse_iot.presentation.viewmodel.login.LoginState
import com.kuliah.greenhouse_iot.presentation.viewmodel.login.LoginViewModel
import com.kuliah.greenhouse_iot.util.rememberImeState


@Composable
fun LoginScreen(
	viewModel: LoginViewModel = hiltViewModel(),
	onLoginSuccess: () -> Unit,
) {
	val loginState by viewModel.loginState.collectAsState()

	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var passwordVisible by remember { mutableStateOf(false) }

	var showAlert by remember { mutableStateOf(false) }
	var alertMessage by remember { mutableStateOf("") }

	val headColor = MaterialTheme.colorScheme.onSurface
	val gradientColor = listOf(Color(0xFF01433d), Color(0xFF0BD953))
	val cornerRadius = 16.dp

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.Transparent)
	) {
		Image(
			painter = painterResource(id = R.drawable.login),
			contentDescription = null,
			contentScale = ContentScale.Fit,
			modifier = Modifier
				.height(180.dp)
				.padding(top = 20.dp)
				.fillMaxWidth()
				.align(Alignment.TopCenter)
		)

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
				.verticalScroll(rememberScrollState())
				.align(Alignment.Center),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(modifier = Modifier.height(50.dp))

			Text(
				text = "Log in",
				textAlign = TextAlign.Center,
				modifier = Modifier
					.padding(top = 130.dp)
					.fillMaxWidth(),
				style = MaterialTheme.typography.headlineSmall,
				color = headColor,
			)
			Spacer(modifier = Modifier.height(16.dp))

			// Input Username
			OutlinedTextField(
				value = username,
				onValueChange = { username = it },
				label = { Text("Username") },
				modifier = Modifier.fillMaxWidth(0.8f)
			)
			Spacer(modifier = Modifier.height(16.dp))

			// Input Password
			OutlinedTextField(
				value = password,
				onValueChange = { password = it },
				label = { Text("Password") },
				visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
				trailingIcon = {
					IconButton(onClick = { passwordVisible = !passwordVisible }) {
						Icon(
							imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
							contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
						)
					}
				},
				modifier = Modifier.fillMaxWidth(0.8f)
			)
			Spacer(modifier = Modifier.height(24.dp))

			// Login Button
			GradientButton(
				gradientColors = gradientColor,
				cornerRadius = cornerRadius,
				nameButton = if (loginState is LoginState.Loading) "Logging in..." else "Login",
				roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
				onClick = {
					if (username.isBlank() || password.isBlank()) {
						alertMessage = "Username dan password tidak boleh kosong."
						showAlert = true
					} else {
						viewModel.login(username, password)
					}
				}
			)
		}
	}

	// Tangani status login
	when (loginState) {
		is LoginState.Success -> {
			LaunchedEffect(Unit) {
				onLoginSuccess()
				viewModel.resetLoginState() // Reset state agar tidak memicu ulang
			}
		}
		is LoginState.Error -> {
			alertMessage = when ((loginState as LoginState.Error).message) {
				"HTTP 400" -> "Username atau password salah."
				"No Internet" -> "Tidak ada koneksi internet. Harap periksa jaringan Anda."
				"Unknown Error" -> "Terjadi kesalahan yang tidak diketahui."
				else -> "Kesalahan: ${(loginState as LoginState.Error).message}"
			}
			showAlert = true
			viewModel.resetLoginState() // Reset state setelah menampilkan error
		}
		else -> {}
	}

	// Alert Dialog
	if (showAlert) {
		AlertDialog(
			onDismissRequest = { showAlert = false },
			confirmButton = {
				TextButton(onClick = { showAlert = false }) {
					Text("OK")
				}
			},
			title = { Text("Pemberitahuan") },
			text = { Text(alertMessage) }
		)
	}
}


@Composable
private fun GradientButton(
	gradientColors: List<Color>,
	cornerRadius: Dp,
	nameButton: String,
	roundedCornerShape: RoundedCornerShape,
	onClick: () -> Unit
) {
	Button(
		modifier = Modifier
			.fillMaxWidth(0.8f)
			.padding(horizontal = 32.dp),
		onClick = onClick,
		contentPadding = PaddingValues(),
		colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
		shape = roundedCornerShape
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					brush = Brush.horizontalGradient(colors = gradientColors),
					shape = roundedCornerShape
				)
				.clip(roundedCornerShape)
				.padding(horizontal = 16.dp, vertical = 8.dp),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = nameButton,
				fontSize = 20.sp,
				color = Color.White
			)
		}
	}
}
