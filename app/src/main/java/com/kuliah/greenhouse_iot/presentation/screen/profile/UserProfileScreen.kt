package com.kuliah.greenhouse_iot.presentation.screen.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.AktuatorData
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.model.SetPointData
import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.presentation.viewmodel.history.HistoryViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.user.UserManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(viewModel: UserManagementViewModel = hiltViewModel()) {
	val userList by viewModel.userList.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()
	val errorMessage by viewModel.errorMessage.collectAsState()

	// Menampilkan layar loading atau error jika diperlukan
	Scaffold(
		topBar = {
			TopAppBar(title = { Text("User Profile") })
		}
	) { padding ->
		Box(modifier = Modifier
			.padding(padding)
			.fillMaxSize()
			.background(Color.LightGray)) {

			// Menampilkan indikator loading jika data masih dimuat
			if (isLoading) {
				CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
