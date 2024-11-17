//package com.kuliah.greenhouse_iot.presentation.viewmodel.mqtt
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
//import com.kuliah.greenhouse_iot.util.ConnectionStatus
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
//@HiltViewModel
//abstract class BaseViewModel<T>(
//	private val mqttRepository: MqttRepository,
//) : ViewModel() {
//
//	private val _data = MutableStateFlow<T?>(null)
//	val data: StateFlow<T?> get() = _data.asStateFlow()
//
//	val connectionStatus = mqttRepository.getConnectionStatus().stateIn(
//		viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectionStatus.DISCONNECTED
//	)
//
//	init {
//		subscribeData()
//	}
//
//	abstract fun subscribeFlow(): Flow<T>
//
//	private fun subscribeData() {
//		viewModelScope.launch {
//			subscribeFlow().collect { receivedData ->
//				_data.value = receivedData
//			}
//		}
//	}
//
//	fun reconnect() {
//		viewModelScope.launch {
//			mqttRepository.reconnect()
//		}
//	}
//}
