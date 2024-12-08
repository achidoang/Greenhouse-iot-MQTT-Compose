package com.kuliah.greenhouse_iot.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
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

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("User Profile", style = MaterialTheme.typography.titleLarge) },
				actions = {
					IconButton(onClick = { authViewModel.logout() }) {
						Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
					}
				},
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
				)
			)
		},
	) { padding ->
		Box(
			modifier = Modifier
				.padding(padding)
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
		) {
			when {
				isLoading -> {
					// Loading indicator
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						LottieLoading()
					}
				}

				!errorMessage.isNullOrEmpty() -> {
					// Error message
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = errorMessage ?: "An error occurred",
							color = MaterialTheme.colorScheme.error,
							style = MaterialTheme.typography.bodyLarge,
							textAlign = TextAlign.Center
						)
					}
				}

				else -> {
					// User list
					LazyColumn(
						modifier = Modifier.fillMaxSize(),
						contentPadding = PaddingValues(16.dp),
						verticalArrangement = Arrangement.spacedBy(12.dp)
					) {
						items(userList) { user ->
							UserProfileItem(user = user)
						}
					}
				}
			}
		}
	}

	LaunchedEffect(true) {
		viewModel.loadAllUsers()
	}
}

@Composable
fun UserProfileItem(user: User) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		elevation = CardDefaults.elevatedCardElevation(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.onSurface
		)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			// Avatar
			Box(
				modifier = Modifier
					.size(48.dp)
					.background(MaterialTheme.colorScheme.primary, CircleShape),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = user.username.take(1).uppercase(),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onPrimary
				)
			}

			Spacer(modifier = Modifier.width(16.dp))

			// User details
			Column(
				verticalArrangement = Arrangement.spacedBy(4.dp),
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = user.fullname,
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = user.email,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Text(
					text = "Role: ${user.role}",
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}
