package com.kuliah.greenhouse_iot.presentation.viewmodel.login

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.local.datastore.AuthDataStoreManager
import com.kuliah.greenhouse_iot.domain.usecases.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val loginUseCase: LoginUseCase,
	private val authDataStoreManager: AuthDataStoreManager
) : ViewModel() {

	private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
	val loginState: StateFlow<LoginState> = _loginState

	fun login(username: String, password: String) {
		viewModelScope.launch {
			_loginState.value = LoginState.Loading
			try {
				val response = loginUseCase(username, password)

				// Simpan token dan role pengguna
				authDataStoreManager.saveAuthToken(response.token)
				val decodedRole = decodeRoleFromToken(response.token) // Fungsi untuk decode JWT
				authDataStoreManager.saveUserRole(decodedRole)

				_loginState.value = LoginState.Success
			} catch (e: IOException) {
				// Kesalahan koneksi internet
				_loginState.value = LoginState.Error("No Internet")
			} catch (e: HttpException) {
				// Kesalahan dari server (HTTP Status Code)
				_loginState.value = LoginState.Error("HTTP ${e.code()}")
			} catch (e: Exception) {
				// Kesalahan lainnya
				_loginState.value = LoginState.Error(e.message ?: "Unknown Error")
			}
		}
	}


	// Fungsi untuk decode role dari token JWT
	private fun decodeRoleFromToken(token: String): String {
		val parts = token.split(".")
		if (parts.size == 3) {
			val payload = parts[1]
			val decodedPayload = Base64.decode(payload, Base64.URL_SAFE)
			val jsonObject = JSONObject(String(decodedPayload))
			return jsonObject.getString("role")
		}
		throw IllegalArgumentException("Invalid token format")
	}

	fun resetLoginState() {
		_loginState.value = LoginState.Idle
	}

}

sealed class LoginState {
	object Idle : LoginState()
	object Loading : LoginState()
	object Success : LoginState()
	data class Error(val message: String) : LoginState()
}
