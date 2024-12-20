package com.kuliah.greenhouse_iot.presentation.screen.add_user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.viewmodel.user.UserManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
	viewModel: UserManagementViewModel = hiltViewModel(),
	onAddSuccess: () -> Unit,
	navController: NavHostController
) {
	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var fullname by remember { mutableStateOf("") }
	var gender by remember { mutableStateOf("Male") }
	var role by remember { mutableStateOf("user") }
	var token by remember { mutableStateOf("token") }

	val isLoading by viewModel.isLoading.collectAsState()
	val errorMessage by viewModel.errorMessage.collectAsState()

	var showPassword by remember { mutableStateOf(false) }
	var isFormValid by remember { mutableStateOf(false) }

	LaunchedEffect(username, password, email, fullname) {
		isFormValid = username.isNotBlank() && password.isNotBlank() &&
				email.isNotBlank() && fullname.isNotBlank()
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Add User") },
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
						Icon(Icons.Default.ArrowBack, contentDescription = "Back")
					}

				},
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = MaterialTheme.colorScheme.surface
			)
		}
	) { padding ->
		Box(modifier = Modifier
			.padding(padding)
			.fillMaxSize()
		) {
			if (isLoading) {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}
			} else {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(rememberScrollState())
						.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(16.dp)
				) {
					OutlinedTextField(
						value = username,
						onValueChange = { username = it },
						label = { Text("Username") },
						singleLine = true,
						modifier = Modifier.fillMaxWidth(),
						leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
					)

					OutlinedTextField(
						value = password,
						onValueChange = { password = it },
						label = { Text("Password") },
						singleLine = true,
						visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
						modifier = Modifier.fillMaxWidth(),
						leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
						trailingIcon = {
							IconButton(onClick = { showPassword = !showPassword }) {
								Icon(
									if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
									contentDescription = if (showPassword) "Hide password" else "Show password"
								)
							}
						}
					)

					OutlinedTextField(
						value = email,
						onValueChange = { email = it },
						label = { Text("Email") },
						singleLine = true,
						modifier = Modifier.fillMaxWidth(),
						leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
					)

					OutlinedTextField(
						value = fullname,
						onValueChange = { fullname = it },
						label = { Text("Full Name") },
						singleLine = true,
						modifier = Modifier.fillMaxWidth(),
						leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
					)

					EnhancedDropdownMenu(
						selectedValue = gender,
						label = "Gender",
						options = listOf("Male", "Female"),
						onOptionSelected = { gender = it }
					)

					EnhancedDropdownMenu(
						selectedValue = role,
						label = "Role",
						options = listOf("user", "admin"),
						onOptionSelected = { role = it }
					)

					Button(
						onClick = {
							val newUser = User(
								username = username,
								password = password,
								email = email,
								fullname = fullname,
								gender = gender,
								role = role,
								token = token,
							)
							viewModel.addUser(newUser)
							onAddSuccess()
						},
						modifier = Modifier.fillMaxWidth(),
						enabled = isFormValid
					) {
						Text("Add User")
					}

					if (!isFormValid) {
						Text(
							text = "Please fill in all fields to submit",
							color = MaterialTheme.colorScheme.error,
							style = MaterialTheme.typography.bodySmall,
							modifier = Modifier.padding(top = 8.dp)
						)
					}

					errorMessage?.let {
						Text(
							text = it,
							color = MaterialTheme.colorScheme.error,
							style = MaterialTheme.typography.bodyMedium,
							modifier = Modifier.padding(top = 8.dp)
						)
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedDropdownMenu(
	selectedValue: String,
	label: String,
	options: List<String>,
	onOptionSelected: (String) -> Unit
) {
	var expanded by remember { mutableStateOf(false) }

	ExposedDropdownMenuBox(
		expanded = expanded,
		onExpandedChange = { expanded = !expanded }
	) {
		OutlinedTextField(
			value = selectedValue,
			onValueChange = {},
			readOnly = true,
			label = { Text(label) },
			trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
			modifier = Modifier.fillMaxWidth().menuAnchor()
		)

		ExposedDropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false }
		) {
			options.forEach { option ->
				DropdownMenuItem(
					onClick = {
						onOptionSelected(option)
						expanded = false
					},
					content = { Text(option) }
				)
			}
		}
	}
}

