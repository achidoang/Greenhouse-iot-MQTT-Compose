package com.kuliah.greenhouse_iot.presentation.viewmodel.monitoring

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.subscribe.MonitoringData
import com.kuliah.greenhouse_iot.data.service.MonitoringService
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.SubscribeMonitoringUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
	private val subscribeMonitoringUseCase: SubscribeMonitoringUseCase,
	@ApplicationContext private val context: Context
) : ViewModel() {

	val monitoringData: StateFlow<MonitoringData?> = subscribeMonitoringUseCase()
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	private val _deviceStatus = MutableStateFlow("Online") // Status awal
	val deviceStatus: StateFlow<String> = _deviceStatus
	private var lastReceivedTime: Long = System.currentTimeMillis()
	private val TIMEOUT = 4_000L // 4 detik timeout

	private var isInternetAvailable = true // Mengatur status internet, defaultnya online

	init {
		viewModelScope.launch {
			MonitoringService().serviceStatus.collect { status ->
				_deviceStatus.value = status
			}
		}
	}


	init {
		viewModelScope.launch {
			monitoringData.collect { data ->
				if (data != null) {
					// Data ada, set status online
					_deviceStatus.value = "Online"
					lastReceivedTime = System.currentTimeMillis()
					isInternetAvailable = true // Pastikan ada koneksi internet
				}
			}
		}

		// Cek timeout secara terpisah di thread lain
		viewModelScope.launch {
			while (true) {
				delay(1000L) // Cek setiap detik
				if (System.currentTimeMillis() - lastReceivedTime > TIMEOUT) {
					_deviceStatus.value = "Offline"
				}
			}
		}

		// Cek status koneksi internet
		viewModelScope.launch {
			while (true) {
				delay(500L) // Cek setiap setengah detik
				isInternetAvailable = isConnectedToInternet() // Cek status internet
				_deviceStatus.value = if (!isInternetAvailable) "No Internet" else _deviceStatus.value
			}
		}
	}

	private fun isConnectedToInternet(): Boolean {
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetwork = connectivityManager.activeNetworkInfo
		return activeNetwork != null && activeNetwork.isConnected
	}

	// Fungsi untuk memulai MonitoringService
	fun startMonitoringService() {
		val intent = Intent(context, MonitoringService::class.java)
		ContextCompat.startForegroundService(context, intent)
	}

	// Fungsi untuk menghentikan MonitoringService
	fun stopMonitoringService() {
		val intent = Intent(context, MonitoringService::class.java)
		context.stopService(intent)
	}
}



