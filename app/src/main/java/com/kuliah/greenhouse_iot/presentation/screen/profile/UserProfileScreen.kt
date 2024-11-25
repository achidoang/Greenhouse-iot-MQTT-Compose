package com.kuliah.greenhouse_iot.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.viewmodel.auth.AuthViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.user.UserManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
	viewModel: UserManagementViewModel = hiltViewModel(),
	authViewModel: AuthViewModel = hiltViewModel()
) {
	val userList by viewModel.userList.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()
	val errorMessage by viewModel.errorMessage.collectAsState()

	// Menampilkan layar loading atau error jika diperlukan
	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("User Profile", style = MaterialTheme.typography.titleLarge) },
				actions = {
					IconButton(onClick = { authViewModel.logout() }) { // Logout action
						Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
					}
				},
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer
				)
			)
		},
	) { padding ->
		Box(
			modifier = Modifier
				.padding(padding)
				.fillMaxSize()
				.background(Color.LightGray)
		) {

			// Menampilkan indikator loading jika data masih dimuat
			if (isLoading) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}

			}
			// Menampilkan pesan error jika ada
			else if (!errorMessage.isNullOrEmpty()) {
				Text(
					text = errorMessage ?: "Error",
					color = MaterialTheme.colorScheme.error,
					modifier = Modifier.align(Alignment.Center)
				)
			}
			// Menampilkan daftar pengguna
			else {
				LazyColumn(
					modifier = Modifier.fillMaxSize(),
					contentPadding = PaddingValues(16.dp)
				) {
					items(userList) { user ->
						UserProfileItem(user = user)
					}
				}
			}
		}
	}

	// Memastikan data pengguna dimuat ketika screen pertama kali dibuka
	LaunchedEffect(true) {
		viewModel.loadAllUsers()
	}
}

@Composable
fun UserProfileItem(user: User) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		//		elevation = 4.dp
	) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Text(text = "Username: ${user.username}", style = MaterialTheme.typography.bodyLarge)
			Text(text = "Full Name: ${user.fullname}", style = MaterialTheme.typography.bodyMedium)
			Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
			Text(text = "Role: ${user.role}", style = MaterialTheme.typography.bodyMedium)
			Text(text = "Gender: ${user.gender}", style = MaterialTheme.typography.bodyMedium)
		}
	}
}
