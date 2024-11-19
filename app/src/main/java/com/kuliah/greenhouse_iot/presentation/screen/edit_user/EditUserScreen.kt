package com.kuliah.greenhouse_iot.presentation.screen.edit_user

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.presentation.screen.add_user.DropdownMenuComponent
import com.kuliah.greenhouse_iot.presentation.viewmodel.user.UserManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
	userId: Int,
	viewModel: UserManagementViewModel = hiltViewModel(),
	onEditSuccess: () -> Unit
) {
	val currentUser by viewModel.currentUser.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()
	val errorMessage by viewModel.errorMessage.collectAsState()

	LaunchedEffect(userId) {
		viewModel.loadUserById(userId)
		Log.d("EditUserScreen", "Loaded user: ${viewModel.currentUser.value}")
	}


	Scaffold(
		topBar = {
			TopAppBar(title = { Text("Edit User") })
		}
	) { padding ->
		Box(modifier = Modifier.padding(padding).fillMaxSize()) {
			if (isLoading) {
				CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
			} else if (currentUser != null) {
				val user = currentUser!!
				var username by remember { mutableStateOf(user.username) }
				var email by remember { mutableStateOf(user.email) }
				var fullname by remember { mutableStateOf(user.fullname) }
				var gender by remember { mutableStateOf(user.gender) }
				var role by remember { mutableStateOf(user.role) }

				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					TextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
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
							if (username.isNotBlank() && email.isNotBlank() && fullname.isNotBlank()) {
								val updatedUser = user.copy(
									username = username,
									email = email,
									fullname = fullname,
									gender = gender,
									role = role
								)
								viewModel.updateUser(userId, updatedUser)
								onEditSuccess()
							} else {
								Log.e("EditUserScreen", "Validation failed: Missing fields")
							}
						},
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Update User")
					}


					errorMessage?.let {
						Text(text = it, color = MaterialTheme.colorScheme.error)
					}
				}
			}
		}
	}
}
