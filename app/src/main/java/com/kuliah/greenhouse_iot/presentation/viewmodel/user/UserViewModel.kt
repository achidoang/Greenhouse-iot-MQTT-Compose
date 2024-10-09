package com.kuliah.greenhouse_iot.presentation.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.LoginRequest
import com.kuliah.greenhouse_iot.data.model.LoginResponse
import com.kuliah.greenhouse_iot.data.model.RegisterRequest
import com.kuliah.greenhouse_iot.data.model.RegisterResponse
import com.kuliah.greenhouse_iot.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
	private val userRepository: UserRepository
) : ViewModel() {

	private val _loginResponse = MutableLiveData<LoginResponse>()
	val loginResponse: LiveData<LoginResponse> get() = _loginResponse

	private val _registerResponse = MutableLiveData<RegisterResponse>()
	val registerResponse: LiveData<RegisterResponse> get() = _registerResponse

	fun login(username: String, password: String) = viewModelScope.launch {
		val response = userRepository.loginUser(LoginRequest(username, password))
		if (response.isSuccessful) {
			_loginResponse.postValue(response.body())
		}
	}

	fun register(username: String, password: String, role: String) = viewModelScope.launch {
		val response = userRepository.registerUser(RegisterRequest(username, password, role))
		if (response.isSuccessful) {
			_registerResponse.postValue(response.body())
		}
	}
}
