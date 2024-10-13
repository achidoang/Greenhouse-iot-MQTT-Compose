package com.kuliah.greenhouse_iot.presentation.viewmodel.connectMqtt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.domain.usecases.connect.ConnectToBrokerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
	private val connectToBrokerUseCase: ConnectToBrokerUseCase
) : ViewModel() {

	private val _isConnected = MutableLiveData<Boolean>()
	val isConnected: LiveData<Boolean> = _isConnected

	init {
		checkConnectionStatus()
	}

	private fun checkConnectionStatus() {
		viewModelScope.launch {
			_isConnected.value = connectToBrokerUseCase().isSuccess
		}
	}

	fun connectToBroker() {
		viewModelScope.launch {
			val result = connectToBrokerUseCase()
			_isConnected.value = result.isSuccess
		}
	}
}
