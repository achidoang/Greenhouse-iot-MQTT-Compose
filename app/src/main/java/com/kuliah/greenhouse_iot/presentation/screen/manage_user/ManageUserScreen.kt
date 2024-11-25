package com.kuliah.greenhouse_iot.presentation.screen.manage_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.presentation.viewmodel.auth.AuthViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.user.UserManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUserScreen(
	userManagementViewModel: UserManagementViewModel = hiltViewModel(),
	authViewModel: AuthViewModel = hiltViewModel(),
	onAddUser: () -> Unit,
	onEditUser: (User) -> Unit,
	onLogout: () -> Unit
) {
	val userList by userManagementViewModel.userList.collectAsState()
	val isLoading by userManagementViewModel.isLoading.collectAsState()
	val errorMessage by userManagementViewModel.errorMessage.collectAsState()
	var selectedUser by remember { mutableStateOf<User?>(null) } // Untuk dialog "Read Data"
	var userToDelete by remember { mutableStateOf<User?>(null) }
	val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()

	LaunchedEffect(isUserLoggedIn) {
		if (!isUserLoggedIn) {
			onLogout() // Navigasi ke screen login jika sudah logout
		}
	}

	LaunchedEffect(Unit) {
		userManagementViewModel.loadAllUsers()
	}

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("Manage Users", style = MaterialTheme.typography.titleLarge) },
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
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = {
			FloatingActionButton(
				onClick = onAddUser,
				modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 70.dp), // Jaga jarak FAB dari sisi layar
				containerColor = MaterialTheme.colorScheme.primary,
				contentColor = MaterialTheme.colorScheme.onPrimary
			) {
				Icon(Icons.Default.Add, contentDescription = "Add User")
			}
		}
	) { innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
				.background(Color.Gray)
		) {
			when {
				isLoading -> CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
					color = MaterialTheme.colorScheme.primary
				)
				errorMessage != null -> Text(
					text = errorMessage ?: "Error",
					color = MaterialTheme.colorScheme.error,
					modifier = Modifier.align(Alignment.Center),
					style = MaterialTheme.typography.bodyLarge
				)
				userList.isEmpty() -> Text(
					text = "No users available",
					modifier = Modifier.align(Alignment.Center),
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.onBackground
				)
				else -> LazyColumn(
					modifier = Modifier.padding(8.dp)
				) {
					items(userList, key = { it.id!! }) { user ->
						UserItem(
							user = user,
							onReadClick = { selectedUser = user }, // Buka dialog "Read Data"
							onEditClick = { onEditUser(user) },
							onDeleteClick = { userToDelete = user },
							isCurrentUser = userManagementViewModel.isCurrentUser(user)
						)
					}
				}
			}

			// Dialog "Read Data"
			selectedUser?.let { user ->
				UserDetailsDialog(
					user = user,
					onDismiss = { selectedUser = null }
				)
			}

			// Dialog Konfirmasi Hapus
			userToDelete?.let { user ->
				ConfirmDeleteDialog(
					user = user,
					onConfirm = {
						userManagementViewModel.deleteUser(user.id ?: 0)
						userToDelete = null // Tutup dialog setelah penghapusan
					},
					onDismiss = { userToDelete = null } // Tutup dialog tanpa tindakan
				)
			}
		}
	}
}

@Composable
fun UserItem(
	user: User,
	onReadClick: () -> Unit,
	onEditClick: () -> Unit,
	onDeleteClick: () -> Unit,
	isCurrentUser: Boolean
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
		colors = CardDefaults.cardColors(
			containerColor = if (isCurrentUser) MaterialTheme.colorScheme.secondaryContainer
			else MaterialTheme.colorScheme.surface
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Column {
				Text(
					text = user.username,
					style = MaterialTheme.typography.titleMedium,
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
			Row {
				IconButton(onClick = onReadClick) {
					Icon(Icons.Default.Visibility, contentDescription = "Read", tint = MaterialTheme.colorScheme.primary)
				}
				IconButton(onClick = onEditClick) {
					Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.secondary)
				}
				IconButton(onClick = onDeleteClick) {
					Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
				}
			}
			if (isCurrentUser) {
				Text(
					text = "Current User",
					style = MaterialTheme.typography.labelSmall,
					color = MaterialTheme.colorScheme.primary
				)
			}
		}
	}
}

@Composable
fun UserDetailsDialog(user: User, onDismiss: () -> Unit) {
	AlertDialog(
		onDismissRequest = onDismiss,
		title = {
			Text(
				text = "User Details",
				style = MaterialTheme.typography.titleLarge,
				color = MaterialTheme.colorScheme.primary
			)
		},
		text = {
			Column(modifier = Modifier.padding(8.dp)) {
				Text(text = "Username: ${user.username}", style = MaterialTheme.typography.bodyLarge)
				Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
				Text(text = "Role: ${user.role}", style = MaterialTheme.typography.bodyMedium)
				Text(text = "Gender: ${user.gender}", style = MaterialTheme.typography.bodyMedium)
			}
		},
		confirmButton = {
			TextButton(onClick = onDismiss) {
				Text("Close", style = MaterialTheme.typography.labelLarge)
			}
		}
	)
}

@Composable
fun ConfirmDeleteDialog(
	user: User,
	onConfirm: () -> Unit,
	onDismiss: () -> Unit
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		title = {
			Text(text = "Delete User", style = MaterialTheme.typography.titleMedium)
		},
		text = {
			Text(
				text = "Are you sure you want to delete ${user.username}?",
				style = MaterialTheme.typography.bodyMedium
			)
		},
		confirmButton = {
			TextButton(onClick = onConfirm) {
				Text("Yes", color = MaterialTheme.colorScheme.error)
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text("Cancel")
			}
		}
	)
}

