package com.kuliah.greenhouse_iot.presentation.screen.add_user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.viewmodel.user.UserManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
	viewModel: UserManagementViewModel = hiltViewModel(),
	onAddSuccess: () -> Unit
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

	Scaffold(
		topBar = {
			TopAppBar(title = { Text("Add User") })
		}
	) { padding ->
		Box(modifier = Modifier.padding(padding).fillMaxSize()) {
			if (isLoading) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}
			} else {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					TextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
					TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
					TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
					TextField(value = fullname, onValueChange = { fullname = it }, label = { Text("Fullname") })
					DropdownMenuComponent(
						selectedValue = gender,
						label = "Gender",
						options = listOf("Male", "Female"),
						onOptionSelected = { gender = it }
					)
					DropdownMenuComponent(
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
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Add User")
					}

					errorMessage?.let {
						Text(text = it, color = MaterialTheme.colorScheme.error)
					}
				}
			}
		}
	}
}

@Composable
fun DropdownMenuComponent(
	selectedValue: String,
	label: String,
	options: List<String>,
	onOptionSelected: (String) -> Unit
) {
	var expanded by remember { mutableStateOf(false) }

	Column {
		Text(text = label, style = MaterialTheme.typography.labelMedium)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.clickable { expanded = true }
				.background(MaterialTheme.colorScheme.surface)
				.padding(12.dp)
		) {
			Text(text = selectedValue, style = MaterialTheme.typography.bodyMedium)
		}
		DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
			options.forEach { option ->
				DropdownMenuItem(onClick = {
					expanded = false
					onOptionSelected(option)
				}) {
					Text(option)
				}
			}
		}
	}
}
