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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

//@Composable
//fun LoginScreen(
//	viewModel: LoginViewModel = hiltViewModel(),
//	onLoginSuccess: () -> Unit
//) {
//	val isImeVisible by rememberImeState()
//	val loginState by viewModel.loginState.collectAsState()
//
//	var username by remember { mutableStateOf("") }
//	var password by remember { mutableStateOf("") }
//
//	val scaffoldState = rememberScaffoldState()
//
//	val headColor = MaterialTheme.colorScheme.onSurface
//	val textColor = MaterialTheme.colorScheme.onBackground
//	val bgColor = MaterialTheme.colorScheme.background
//	val secBgColor = MaterialTheme.colorScheme.tertiaryContainer
//	val primary = MaterialTheme.colorScheme.primary
//
//	Scaffold(
//		scaffoldState = scaffoldState,
//		snackbarHost = {
//			SnackbarHost(
//				hostState = it,
//				modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.Top),
//				snackbar = { data ->
//					Snackbar(
//						snackbarData = data,
//						backgroundColor = Color(0xFFEB5757),
//						contentColor = headColor,
//						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//					)
//				}
//			)
//		}
//	) { padding ->
//		GradientBox(modifier = Modifier.fillMaxSize()) {
//			Column(
//				modifier = Modifier
//					.fillMaxSize()
//					.padding(padding),
//				horizontalAlignment = Alignment.CenterHorizontally
//			) {
//				val animatedUpperSectionRatio by animateFloatAsState(
//					targetValue = if (isImeVisible) 0f else 0.35f,
//					label = "",
//				)
//				AnimatedVisibility(visible = !isImeVisible, enter = fadeIn(), exit = fadeOut()) {
//					Box(
//						modifier = Modifier
//							.fillMaxWidth()
//							.fillMaxHeight(animatedUpperSectionRatio),
//						contentAlignment = Alignment.Center
//					) {
//						Text(
//							text = "Welcome to AutoGreen",
//							style = MaterialTheme.typography.headlineMedium,
//							color = headColor
//						)
//					}
//				}
//				Column(
//					modifier = Modifier
//						.fillMaxSize()
//						.clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
//						.background(bgColor),
//					horizontalAlignment = Alignment.CenterHorizontally
//				) {
//					Spacer(modifier = Modifier.height(16.dp))
//					Text(
//						text = "Log in",
//						style = MaterialTheme.typography.headlineMedium,
//						color = headColor
//					)
//					Spacer(modifier = Modifier.height(24.dp))
//
//					MyTextField(
//						modifier = Modifier.padding(horizontal = 16.dp),
//						value = username,
//						onValueChange = { username = it },
//						label = "Username",
//						keyboardOptions = KeyboardOptions.Default,
//						keyboardActions = KeyboardActions.Default
//					)
//					Spacer(modifier = Modifier.height(20.dp))
//					MyTextField(
//						modifier = Modifier.padding(horizontal = 16.dp),
//						value = password,
//						onValueChange = { password = it },
//						label = "Password",
//						keyboardOptions = KeyboardOptions.Default,
//						keyboardActions = KeyboardActions.Default,
//						trailingIcon = Icons.Default.Lock
//					)
//					Spacer(modifier = Modifier.height(20.dp))
//					Button(
//						onClick = { viewModel.login(username, password) },
//						modifier = Modifier
//							.fillMaxWidth()
//							.padding(horizontal = 16.dp),
//						colors = ButtonDefaults.buttonColors(
//							containerColor = primary,
//							contentColor = headColor
//						),
//						shape = RoundedCornerShape(10.dp)
//					) {
//						if (loginState is LoginState.Loading) {
//							CircularProgressIndicator(
//								color = headColor,
//								modifier = Modifier.size(24.dp)
//							)
//						} else {
//							Text(
//								text = "Log in",
//								style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500)),
//								color = headColor
//							)
//						}
//					}
//				}
//			}
//		}
//
//		// Tangani status login
//		when (loginState) {
//			is LoginState.Success -> {
//				LaunchedEffect(Unit) {
//					onLoginSuccess()
//				}
//			}
//			is LoginState.Error -> {
//				val errorMessage = when ((loginState as LoginState.Error).message) {
//					"HTTP 400" -> "Username atau password salah."
//					"No Internet" -> "Tidak ada koneksi internet. Harap periksa jaringan Anda."
//					"Unknown Error" -> "Terjadi kesalahan yang tidak diketahui."
//					else -> "Kesalahan: ${(loginState as LoginState.Error).message}"
//				}
//				LaunchedEffect(scaffoldState.snackbarHostState) {
//					scaffoldState.snackbarHostState.showSnackbar(errorMessage)
//				}
//			}
//			else -> {}
//		}
//	}
//}



@Composable
fun LoginScreen(
	viewModel: LoginViewModel = hiltViewModel(),
	onLoginSuccess: () -> Unit,
) {
	val isImeVisible by rememberImeState()
	val loginState by viewModel.loginState.collectAsState()

	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	val scaffoldState = rememberScaffoldState()



	val headColor = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground
	val bgColor = MaterialTheme.colorScheme.background
	val secBgColor = MaterialTheme.colorScheme.tertiaryContainer
	val primary = MaterialTheme.colorScheme.primary
	val secon = MaterialTheme.colorScheme.secondary

	val gradientColor = listOf(Color(0xFF484BF1), Color(0xFF673AB7))
	val cornerRadius = 16.dp

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.Transparent)
	) {
		Image(
			painter = painterResource(id = R.drawable.user_sign_in),
			contentDescription = null,
			contentScale = ContentScale.Fit,
			modifier = Modifier
				.height(180.dp)
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
				shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
				label = { Text("Username") },
				keyboardOptions = KeyboardOptions.Default,
				modifier = Modifier.fillMaxWidth(0.8f)
			)
			Spacer(modifier = Modifier.height(16.dp))

			// Input Password
			OutlinedTextField(
				value = password,
				onValueChange = { password = it },
				shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
				label = { Text("Password") },
				visualTransformation = PasswordVisualTransformation(),
				keyboardOptions = KeyboardOptions.Default,
				modifier = Modifier.fillMaxWidth(0.8f)
			)
			Spacer(modifier = Modifier.height(24.dp))

			// Login Button
			GradientButton(
				gradientColors = gradientColor,
				cornerRadius = cornerRadius,
				nameButton = if (loginState is LoginState.Loading) "Logging in..." else "Login",
				roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
				onClick = { viewModel.login(username, password) }
			)

			Spacer(modifier = Modifier.height(16.dp))

		}
	}

	// Tangani status login
	when (loginState) {
		is LoginState.Success -> {
			LaunchedEffect(Unit) { onLoginSuccess() }
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

//email id
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SimpleOutlinedTextFieldSample() {
	val keyboardController = LocalSoftwareKeyboardController.current
	var text by rememberSaveable { mutableStateOf("") }

	OutlinedTextField(
		value = text,
		onValueChange = { text = it },
		shape = RoundedCornerShape(topEnd =12.dp, bottomStart =12.dp),
		label = {
			Text("Name or Email Address",
				color = MaterialTheme.colorScheme.primary,
				style = MaterialTheme.typography.labelMedium,
			) },
		placeholder = { Text(text = "Name or Email Address") },
		keyboardOptions = KeyboardOptions(
			imeAction = ImeAction.Next,
			keyboardType = KeyboardType.Email
		),
		colors = TextFieldDefaults.outlinedTextFieldColors(
			focusedBorderColor = MaterialTheme.colorScheme.primary,
			unfocusedBorderColor = MaterialTheme.colorScheme.primary),
		singleLine = true,
		modifier = Modifier.fillMaxWidth(0.8f),
		keyboardActions = KeyboardActions(
			onDone = {
				keyboardController?.hide()
				// do something here
			}
		)

	)
}
