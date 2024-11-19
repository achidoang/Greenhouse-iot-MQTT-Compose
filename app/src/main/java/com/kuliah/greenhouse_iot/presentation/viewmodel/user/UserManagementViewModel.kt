package com.kuliah.greenhouse_iot.presentation.viewmodel.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.local.datastore.AuthDataStoreManager
import com.kuliah.greenhouse_iot.data.model.auth.User
import com.kuliah.greenhouse_iot.domain.usecases.user.AddUserUseCase
import com.kuliah.greenhouse_iot.domain.usecases.user.DeleteUserUseCase
import com.kuliah.greenhouse_iot.domain.usecases.user.GetAllUsersUseCase
import com.kuliah.greenhouse_iot.domain.usecases.user.GetUserByIdUseCase
import com.kuliah.greenhouse_iot.domain.usecases.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel @Inject constructor(
	private val getAllUsersUseCase: GetAllUsersUseCase,
	private val getUserByIdUseCase: GetUserByIdUseCase,
	private val addUserUseCase: AddUserUseCase,
	private val updateUserUseCase: UpdateUserUseCase,
	private val deleteUserUseCase: DeleteUserUseCase,
	private val authDataStoreManager: AuthDataStoreManager,
) : ViewModel() {

	private val _userList = MutableStateFlow<List<User>>(emptyList())
	val userList: StateFlow<List<User>> = _userList

	private val _currentUser = MutableStateFlow<User?>(null)
	val currentUser: StateFlow<User?> = _currentUser

	private val _isLoading = MutableStateFlow(false)
	val isLoading: StateFlow<Boolean> = _isLoading

	private val _errorMessage = MutableStateFlow<String?>(null)
	val errorMessage: StateFlow<String?> = _errorMessage

	/**
	 * Memuat semua pengguna saat ViewModel dibuat.
	 */
	init {
		loadCurrentUser()
		loadAllUsers()

	}

	private fun loadCurrentUser() {
		viewModelScope.launch {
			authDataStoreManager.getAuthToken().collect { token ->
				if (!token.isNullOrEmpty()) {
					try {
						_isLoading.value = true
						val users = getAllUsersUseCase() // Mendapatkan semua pengguna
						_userList.value = users
						val currentUser = users.find { it.token == token } // Cocokkan berdasarkan token
						_currentUser.value = currentUser
					} catch (e: Exception) {
						_errorMessage.value = e.message
					} finally {
						_isLoading.value = false
					}
				}
			}
		}
	}




	/**
	 * Memuat semua pengguna dari API menggunakan UseCase.
	 */
	fun loadAllUsers() {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val users = getAllUsersUseCase()
				_userList.value = users
			} catch (e: Exception) {
				_errorMessage.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun isCurrentUser(user: User): Boolean {
		return currentUser.value?.id == user.id
	}


	/**
	 * Memuat detail pengguna berdasarkan ID.
	 * @param userId ID pengguna yang ingin dimuat.
	 */
	fun loadUserById(userId: Int) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val user = getUserByIdUseCase(userId)
				_currentUser.value = user
			} catch (e: Exception) {
				_errorMessage.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	/**
	 * Menambahkan pengguna baru menggunakan UseCase.
	 * @param user Data pengguna baru.
	 */
	fun addUser(user: User) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				addUserUseCase(user)
				loadAllUsers() // Refresh daftar pengguna setelah berhasil menambahkan.
			} catch (e: Exception) {
				_errorMessage.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	/**
	 * Memperbarui data pengguna berdasarkan ID.
	 * @param userId ID pengguna yang akan diperbarui.
	 * @param user Data pengguna yang diperbarui.
	 */
	fun updateUser(userId: Int, user: User) {
		if (currentUser.value?.id == userId) {
			_errorMessage.value = "Cannot delete the current logged-in user"
			return
		}
		viewModelScope.launch {
			_isLoading.value = true
			try {
				updateUserUseCase(userId, user)
				loadAllUsers() // Refresh daftar pengguna setelah berhasil memperbarui.
			} catch (e: Exception) {
				_errorMessage.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	/**
	 * Menghapus pengguna berdasarkan ID.
	 * @param userId ID pengguna yang akan dihapus.
	 */
	fun deleteUser(userId: Int) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				deleteUserUseCase(userId)
				loadAllUsers() // Refresh daftar pengguna setelah berhasil menghapus.
			} catch (e: Exception) {
				_errorMessage.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}
	fun clearErrorMessage() {
		_errorMessage.value = null
	}

}

