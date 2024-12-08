package com.kuliah.greenhouse_iot.presentation.screen.manage_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person2
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
import androidx.navigation.NavController
import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.navigation.Route
import com.kuliah.greenhouse_iot.presentation.viewmodel.auth.AuthViewModel
import com.kuliah.greenhouse_iot.presentation.viewmodel.user.UserManagementViewModel
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUserScreen(
	userManagementViewModel: UserManagementViewModel = hiltViewModel(),
	authViewModel: AuthViewModel = hiltViewModel(),
	onAddUser: () -> Unit,
	onEditUser: (User) -> Unit,
	onLogout: () -> Unit,
	navController: NavController
) {
	val userList by userManagementViewModel.userList.collectAsState()
	val isLoading by userManagementViewModel.isLoading.collectAsState()
	val errorMessage by userManagementViewModel.errorMessage.collectAsState()
	var selectedUser by remember { mutableStateOf<User?>(null) }
	var userToDelete by remember { mutableStateOf<User?>(null) }
	val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()

	LaunchedEffect(isUserLoggedIn) {
		if (!isUserLoggedIn) onLogout()
	}

	LaunchedEffect(Unit) {
		userManagementViewModel.loadAllUsers()
	}
	val headColor = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground
	val bgColor = MaterialTheme.colorScheme.background

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Manage Users") },
				navigationIcon = {
					IconButton(onClick = { navController.navigate(Route.Home.destination) }) {
						Icon(Icons.Default.ArrowBack, contentDescription = "Back")
					}
				},
				actions = {
					IconButton(onClick = { authViewModel.logout()}) {
						Icon(
							imageVector = Icons.Default.ExitToApp,
							contentDescription = "Settings",
							tint = textColor
						)
					}
				},
				modifier = Modifier.height(50.dp), // Reduced height
				backgroundColor = bgColor,
				contentColor = headColor
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = onAddUser,
				containerColor = MaterialTheme.colorScheme.primary,
			) {
				Icon(Icons.Default.Add, contentDescription = "Add User")
			}
		}
	) { innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
				.background(bgColor)
		) {
			when {
				isLoading -> Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					LottieLoading()
				}

				errorMessage != null -> Text(
					text = errorMessage ?: "Error",
					color = MaterialTheme.colorScheme.error,
					style = MaterialTheme.typography.bodyLarge,
					modifier = Modifier.align(Alignment.Center)
				)

				userList.isEmpty() -> Text(
					text = "No users available",
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.onBackground,
					modifier = Modifier.align(Alignment.Center)
				)

				else -> LazyColumn(
					modifier = Modifier.fillMaxSize(),
					contentPadding = PaddingValues(16.dp)
				) {
					items(userList, key = { it.id!! }) { user ->
						UserItem(
							user = user,
							onReadClick = { selectedUser = user },
							onEditClick = { onEditUser(user) },
							onDeleteClick = { userToDelete = user },
							isCurrentUser = userManagementViewModel.isCurrentUser(user)
						)
					}
				}
			}

			selectedUser?.let { user ->
				UserDetailsDialog(
					user = user,
					onDismiss = { selectedUser = null }
				)
			}

			userToDelete?.let { user ->
				ConfirmDeleteDialog(
					user = user,
					onConfirm = {
						userManagementViewModel.deleteUser(user.id ?: 0)
						userToDelete = null
					},
					onDismiss = { userToDelete = null }
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
	val headColor = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		colors = CardDefaults.cardColors(
			containerColor = if (isCurrentUser) MaterialTheme.colorScheme.surface
			else MaterialTheme.colorScheme.tertiaryContainer
		)
	) {
		Row(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Column {
				Text(
					text = user.username,
					style = MaterialTheme.typography.titleMedium,
					color = headColor
				)
				Text(
					text = user.email,
					style = MaterialTheme.typography.bodyMedium,
					color = textColor
				)
				Text(
					text = "Role: ${user.role}",
					style = MaterialTheme.typography.bodySmall,
					color = textColor
				)
			}
			Row {
				IconButton(onClick = onReadClick) {
					Icon(Icons.Default.Visibility, contentDescription = "View User", tint = headColor)
				}
				IconButton(onClick = onEditClick) {
					Icon(Icons.Default.Edit, contentDescription = "Edit User", tint = headColor)
				}
				IconButton(onClick = onDeleteClick) {
					Icon(Icons.Default.Delete, contentDescription = "Delete User", tint = headColor)
				}
			}
		}
	}
}


@Composable
fun UserDetailsDialog(user: User, onDismiss: () -> Unit) {
	AlertDialog(
		onDismissRequest = onDismiss,
		containerColor = MaterialTheme.colorScheme.tertiaryContainer,
		title = {
			Text(
				text = "User Details",
				style = MaterialTheme.typography.titleLarge,
				color = MaterialTheme.colorScheme.primary
			)
		},
		text = {
			Column(modifier = Modifier.padding(8.dp)) {
				Text(
					text = "Username: ${user.username}",
					style = MaterialTheme.typography.bodyLarge
				)
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

