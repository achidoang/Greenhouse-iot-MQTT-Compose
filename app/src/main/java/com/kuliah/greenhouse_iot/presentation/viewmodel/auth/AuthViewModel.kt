package com.kuliah.greenhouse_iot.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.local.datastore.AuthDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val authDataStoreManager: AuthDataStoreManager
) : ViewModel() {

	private val _isUserLoggedIn = MutableStateFlow(false)
	val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

	init {
		checkIfUserIsLoggedIn()
	}


	private fun checkIfUserIsLoggedIn() {
		viewModelScope.launch {
			authDataStoreManager.getAuthToken().collect { token ->
				_isUserLoggedIn.value = authDataStoreManager.isTokenValid(token)
				if (!_isUserLoggedIn.value) {
					logout()
				}
			}
		}
	}

	fun logout() {
		viewModelScope.launch {
			authDataStoreManager.clearAuthToken()
		}
	}

}